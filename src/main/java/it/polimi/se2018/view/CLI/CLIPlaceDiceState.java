package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import it.polimi.se2018.view.AbstractPlaceDiceState;

import java.awt.*;

public class CLIPlaceDiceState extends AbstractPlaceDiceState {

    public CLIPlaceDiceState(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness) {
        super(gameTable, ignore, isFromTool, forceLoneliness);
    }

    public CLIPlaceDiceState(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness, int forceDice, boolean shouldSelectNumber) {
        super(gameTable, ignore, isFromTool, forceLoneliness, forceDice, shouldSelectNumber);
    }

    @Override
    public void process(String input) {

        if (input.equals("cancel")) {
            processCancel();
            return;
        } else if (getInternalState() == InternalState.DICE_SELECTION) {
            int selectedDice;
            try {
                selectedDice = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                CLIPrinter.printError(input + " is not a valid dice!");
                getGameTable().setState(this);
                return;
            }

            try {
                processDiceSelection(selectedDice);
            } catch (InputError ie) {
                CLIPrinter.printError(ie.getMessage());
                getGameTable().setState(this);
                return;
            }

        } else if (getInternalState() == InternalState.POSITION_SELECTION) {
            Point point = CLIPrinter.decodePosition(input);
            if (point == null) {
                CLIPrinter.printError("Invalid position!");
                getGameTable().setState(this);
                return;
            }
            try {
                processPositionSelected(point);
            } catch (InputError ie) {
                CLIPrinter.printError(ie.getMessage());
                getGameTable().setState(this);
                return;
            }
        } else if (getInternalState() == InternalState.NUMBER_SELECTION) {
            int selectedNumber;
            try {
                selectedNumber = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                CLIPrinter.printError(input + " is not a valid dice number!");
                getGameTable().setState(this);
                return;
            }

            try {
                processNumberSelected(selectedNumber);
            } catch (InputError ie) {
                CLIPrinter.printError(ie.getMessage());
                getGameTable().setState(this);
                return;
            }

        }
    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {

        if (getInternalState() == InternalState.DICE_SELECTION) {
            CLIPrinter.printQuestion("Select a dice:");
            CLIPrinter.printDraftBoard(getGameTable().getDraftBoardImmutable());
        } else if (getInternalState() == InternalState.POSITION_SELECTION) {
            if (isShouldNotSelectDice()) {
                CLIPrinter.printQuestion("Select where to place the new dice:");
                CLIPrinter.printDice(getGameTable().getDraftBoardImmutable().getDices()[getSelectedDice()]);
                System.out.println("|");
            }
            CLIPrinter.printQuestion("Select a position:");
            CLIPrinter.printSchema(getGameTable().getSchema(getGameTable().getView().getPlayer()));
        } else if (getInternalState() == InternalState.NUMBER_SELECTION) {
            CLIPrinter.printQuestion("This dice was drawn:");
            CLIPrinter.printDice(getGameTable().getDraftBoardImmutable().getDices()[getSelectedDice()]);
            System.out.println("|");
            CLIPrinter.printQuestion("Select the number of the new dice: ");
        }
    }
}
