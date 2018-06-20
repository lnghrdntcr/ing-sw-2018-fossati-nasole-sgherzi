package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.viewEvent.CancelActionEvent;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceSelectingNumberEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

import java.awt.*;

public class CLIPlaceDiceState extends State {
    private SchemaCardFace.Ignore ignore;
    private InternalState internalState;

    private int selectedDice;
    private boolean isFromTool;
    private boolean forceLoneliness;
    private boolean shouldSelectNumber;
    private boolean shouldNotSelectDice;
    private int selectedNumber;

    public CLIPlaceDiceState(CLIGameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness) {
        this(gameTable, ignore, isFromTool, forceLoneliness, -1, false);
    }

    public CLIPlaceDiceState(CLIGameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness, int forceDice, boolean shouldSelectNumber) {
        super(gameTable);
        this.ignore = ignore;
        this.isFromTool = isFromTool;
        this.forceLoneliness = forceLoneliness;
        this.shouldSelectNumber = shouldSelectNumber;
        shouldNotSelectDice = false;

        if (forceDice == -1) {
            internalState = InternalState.DICE_SELECTION;
        } else if(!shouldSelectNumber) {
            internalState = InternalState.POSITION_SELECTION;
            selectedDice = forceDice;
            shouldNotSelectDice = true;
        }else{
            internalState = InternalState.NUMBER_SELECTION;
            selectedDice = forceDice;
        }
    }

    @Override
    public State process(String input) {

        if (input.equals("cancel")) {

            if(shouldNotSelectDice || shouldSelectNumber) this.getGameTable().getView().sendEventToController(new CancelActionEvent(this.getClass().getName(), this.getGameTable().getView().getPlayer(), "" ));

            return new MainMenuState(getGameTable());
        } else if (internalState == InternalState.DICE_SELECTION) {
            try {
                selectedDice = Integer.parseInt(input);
                if (selectedDice < 0 || selectedDice > getGameTable().getDraftBoardImmutable().getDices().length) {
                    CLIPrinter.printError(input + " is not in range!");
                } else {
                    internalState = InternalState.POSITION_SELECTION;
                }
                return this;
            } catch (NumberFormatException ex) {
                CLIPrinter.printError(input + " is not a valid dice!");
            }
            return this;
        } else if (internalState == InternalState.POSITION_SELECTION) {
            Point point = CLIPrinter.decodePosition(input);
            if (point == null) {
                CLIPrinter.printError("Invalid position!");
                return this;
            }

            DiceFace diceFace = getGameTable().getDraftBoardImmutable().getDices()[selectedDice];

            if (getGameTable().getSchemas(getGameTable().getView().getPlayer()).isDiceAllowed(point, diceFace, ignore, forceLoneliness)) {
                if (shouldNotSelectDice) {
                    getGameTable().getView().sendEventToController(new PlaceAnotherDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName("FirmPastaBrush"), point, selectedDice));
                }else if(shouldSelectNumber){
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
                return new MainMenuState(getGameTable());

            } else {
                CLIPrinter.printError("This dice cannot be placed here!");
                return this;
            }
        } else if (internalState == InternalState.NUMBER_SELECTION) {
            try {
                selectedNumber = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                CLIPrinter.printError(input + " is not a valid dice number!");
                return this;
            }
            
            if(selectedNumber<=0||selectedNumber>6){
                CLIPrinter.printError(input + " not between 1 and 6!");
                return this;
            }
            
            internalState=InternalState.POSITION_SELECTION;

            return this;
        }
        return this;
    }

    @Override
    public void render() {
        if (internalState == InternalState.DICE_SELECTION) {
            CLIPrinter.printQuestion("Select a dice:");
            CLIPrinter.printDraftBoard(getGameTable().getDraftBoardImmutable());
        } else if (internalState == InternalState.POSITION_SELECTION) {
            if (shouldNotSelectDice) {
                CLIPrinter.printQuestion("Select where to place the new dice:");
                CLIPrinter.printDice(getGameTable().getDraftBoardImmutable().getDices()[selectedDice]);
                System.out.println("|");
            }
            CLIPrinter.printQuestion("Select a position:");
            CLIPrinter.printSchema(getGameTable().getSchemas(getGameTable().getView().getPlayer()));
        }else if (internalState == InternalState.NUMBER_SELECTION) {
            CLIPrinter.printQuestion("Select the number of the new dice: ");
        }
    }

    private enum InternalState {DICE_SELECTION, POSITION_SELECTION, NUMBER_SELECTION}
}
