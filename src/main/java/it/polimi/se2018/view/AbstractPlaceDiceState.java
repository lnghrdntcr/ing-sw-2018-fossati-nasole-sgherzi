package it.polimi.se2018.view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIMoveDice;
import it.polimi.se2018.view.CLI.CLIPlaceDiceState;
import it.polimi.se2018.view.viewEvent.CancelActionEvent;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceSelectingNumberEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

import java.awt.*;

public abstract  class AbstractPlaceDiceState extends State {
    private SchemaCardFace.Ignore ignore;
    private InternalState internalState;

    private int selectedDice;
    private boolean isFromTool;
    private boolean forceLoneliness;
    private boolean shouldSelectNumber;
    private boolean shouldNotSelectDice;
    private int selectedNumber;

    protected AbstractPlaceDiceState(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness) {
        this(gameTable, ignore, isFromTool, forceLoneliness, -1, false);
    }

    protected AbstractPlaceDiceState(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness, int forceDice, boolean shouldSelectNumber) {
        super(gameTable);
        this.ignore = ignore;
        this.isFromTool = isFromTool;
        this.forceLoneliness = forceLoneliness;
        this.shouldSelectNumber = shouldSelectNumber;
        shouldNotSelectDice = false;

        if (forceDice == -1) {
            internalState = InternalState.DICE_SELECTION;
        } else if (!shouldSelectNumber) {
            internalState = InternalState.POSITION_SELECTION;
            selectedDice = forceDice;
            shouldNotSelectDice = true;
        } else {
            internalState = InternalState.NUMBER_SELECTION;
            selectedDice = forceDice;
        }
    }

    public static AbstractPlaceDiceState createFromContext(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness){
        if(gameTable.getView().getGraphics()==RemoteView.Graphics.GUI){
            //TODO: change this
            return new CLIPlaceDiceState(gameTable, ignore,isFromTool, forceLoneliness);
        }else{
            return new CLIPlaceDiceState(gameTable, ignore,isFromTool, forceLoneliness);
        }
    }

    public static AbstractPlaceDiceState createFromContext(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness, int forceDice, boolean shouldSelectNumber){
        if(gameTable.getView().getGraphics()==RemoteView.Graphics.GUI){
            //TODO: change this
            return new CLIPlaceDiceState(gameTable, ignore, isFromTool, forceLoneliness, forceDice, shouldSelectNumber);
        }else{
            return new CLIPlaceDiceState(gameTable, ignore, isFromTool, forceLoneliness, forceDice, shouldSelectNumber);
        }
    }

    public void processCancel(){
        if (shouldNotSelectDice || shouldSelectNumber)
            this.getGameTable().getView().sendEventToController(new CancelActionEvent(this.getClass().getName(), this.getGameTable().getView().getPlayer(), ""));

        getGameTable().setState(new CLIMainMenuState(getGameTable()));
    }

    public void processDiceSelection(int selectedDice){
        this.selectedDice=selectedDice;
        if (selectedDice < 0 || selectedDice > getGameTable().getDraftBoardImmutable().getDices().length) {
           throw new InputError(selectedDice + " is not in range!");
        } else {
            internalState = InternalState.POSITION_SELECTION;
            getGameTable().setState(this);
        }

    }

    public void processPositionSelected(Point point){

        DiceFace diceFace = getGameTable().getDraftBoardImmutable().getDices()[selectedDice];

        if (getGameTable().getSchema(getGameTable().getView().getPlayer()).isDiceAllowed(point, diceFace, ignore, forceLoneliness)) {
            if (shouldNotSelectDice) {
                getGameTable().getView().sendEventToController(new PlaceAnotherDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName("FirmPastaBrush"), point, selectedDice));
            } else if (shouldSelectNumber) {
                getGameTable().getView().sendEventToController(new PlaceAnotherDiceSelectingNumberEvent(getClass().getName(), "", getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName("FirmPastaDiluent"), point, selectedDice, selectedNumber));
            } else if (isFromTool && forceLoneliness) {
                getGameTable().getView().sendEventToController(
                        new PlaceAnotherDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(),
                                getGameTable().getToolIndexByName("CorkRow"), point, selectedDice));
            } else if (isFromTool && !forceLoneliness) {
                getGameTable().getView().sendEventToController(
                        new PlaceAnotherDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(),
                                getGameTable().getToolIndexByName("WheeledPincer"), point, selectedDice));
            } else {
                getGameTable().getView().sendEventToController(new PlaceDiceEvent(getClass().getName(), "",
                        getGameTable().getView().getPlayer(), selectedDice, point));
            }
            getGameTable().setState(new CLIMainMenuState(getGameTable()));

        } else {
            throw new InputError("This dice cannot be placed here!");
        }
    }

    public void processNumberSelected(int selectedNumber){
        this.selectedNumber= selectedNumber;
        if (selectedNumber <= 0 || selectedNumber > 6) {
            throw new InputError(selectedNumber + " not between 1 and 6!");
        }

        internalState = InternalState.POSITION_SELECTION;
        getGameTable().setState(this);
    }

    public InternalState getInternalState() {
        return internalState;
    }

    public int getSelectedNumber() {
        return selectedNumber;
    }

    public boolean isFromTool() {
        return isFromTool;
    }

    public boolean isForceLoneliness() {
        return forceLoneliness;
    }

    public boolean isShouldSelectNumber() {
        return shouldSelectNumber;
    }

    public boolean isShouldNotSelectDice() {
        return shouldNotSelectDice;
    }

    public int getSelectedDice() {
        return selectedDice;
    }

    public enum InternalState {DICE_SELECTION, POSITION_SELECTION, NUMBER_SELECTION}
}
