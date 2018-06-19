package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

import java.awt.*;

public class PlaceDiceState extends State {
    private SchemaCardFace.Ignore ignore;
    private InternalState internalState;

    private int selectedDice;
    private boolean isFromTool;
    private boolean forceLoneliness;

    public PlaceDiceState(CLIGameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness) {
        super(gameTable);
        this.ignore = ignore;
        this.isFromTool = isFromTool;
        this.forceLoneliness = forceLoneliness;
        internalState = InternalState.DICE_SELECTION;
    }

    @Override
    public State process(String input) {
        if (input.equals("cancel")) {
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

            if (getGameTable().getSchemas(getGameTable().getView().getPlayer()).isDiceAllowed(point, diceFace, ignore)) {
                //todo gestisci indice tool dai bool
                getGameTable().getView().sendEventToController((isFromTool?
                        new PlaceAnotherDiceEvent(getClass().getName(), "", getGameTable().getCurrentPlayer(), getGameTable().getToolIndexByName("WheeledPincer"), point, selectedDice)
                        : new PlaceDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(), selectedDice, point)));
                return new MainMenuState(getGameTable());
            } else {
                CLIPrinter.printError("This dice cannot be placed here!");
                return this;
            }
        }
        return this;
    }

    @Override
    public void render() {
        if (internalState == InternalState.DICE_SELECTION) {
            CLIPrinter.printQuestion("Select a dice:");
            CLIPrinter.printDraftBoard(getGameTable().getDraftBoardImmutable());
        } else if (internalState == InternalState.POSITION_SELECTION) {
            CLIPrinter.printQuestion("Select a position:");
            CLIPrinter.printSchema(getGameTable().getSchemas(getGameTable().getView().getPlayer()));
        }
    }

    private enum InternalState {DICE_SELECTION, POSITION_SELECTION}
}
