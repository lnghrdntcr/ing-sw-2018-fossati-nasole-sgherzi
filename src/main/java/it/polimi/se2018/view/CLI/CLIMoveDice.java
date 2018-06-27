package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import it.polimi.se2018.view.MoveDice;

import java.awt.*;

public class CLIMoveDice extends MoveDice {

    public CLIMoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times) {
        super(gameTable, ignore, toolName, times);
    }

    public CLIMoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times, GameColor color) {
        super(gameTable, ignore, toolName, times, color);
    }

    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")) {
            getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
            return;
        }

        if (getActionState() == ActionState.CHOOSE) {

            if (getTimes() == Times.FIRST) {
                Point firstSource = CLIPrinter.decodePosition(input);

                if (firstSource == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                try {
                    processFirstSource(firstSource);
                } catch (InputError ie) {
                    CLIPrinter.printError(ie.getMessage());
                    return;
                }


            } else if (getTimes() == Times.SECOND) {

                Point secondSource = CLIPrinter.decodePosition(input);

                if (secondSource == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                try {
                    processSecondSource(secondSource);
                } catch (InputError ie) {
                    CLIPrinter.printError(ie.getMessage());
                    return;
                }

            }

            return;
        } else if (getActionState() == ActionState.PLACE) {

            if (getTimes() == Times.FIRST) {

                Point firstDestination = CLIPrinter.decodePosition(input);

                if (firstDestination == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                try {
                    processFirstDestination(firstDestination);
                } catch (InputError ie) {
                    CLIPrinter.printError(ie.getMessage());
                    return;
                }
            } else if (getTimes() == Times.SECOND) {

                Point secondDestination = CLIPrinter.decodePosition(input);

                if (secondDestination == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                try {
                    processSecondDestination(secondDestination);
                } catch (InputError ie) {
                    CLIPrinter.printError(ie.getMessage());
                    return;
                }
            }

        }


    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {

        if (getActionState() == ActionState.CHOOSE) {

            CLIPrinter.printQuestion("Choose the " + this.getTimes().toString().toLowerCase() + " dice to move: ");
            CLIPrinter.printSchema(getTemporaryPlayerSchema());
        } else {

            CLIPrinter.printQuestion("Choose the destination: ");

        }

    }
}
