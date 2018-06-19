package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.CellRestriction;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.network.Client;
import it.polimi.se2018.view.viewEvent.DoubleMoveDiceEvent;
import it.polimi.se2018.view.viewEvent.MoveDiceEvent;

import java.awt.*;

public class CLIMoveDice extends State {

    SchemaCardFace.Ignore ignore;
    String toolName;
    private Times times;
    private ActionState actionState;

    private Point firstSource;
    private Point secondSource;
    private Point firstDestination;
    private Point secondDestination;
    private Schema playerSchema;

    public CLIMoveDice(CLIGameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times) {
        super(gameTable);
        this.ignore = ignore;
        this.toolName = toolName;
        this.times = times;
        actionState = ActionState.CHOOSE;

        playerSchema = this.getGameTable().getSchemas(this.getGameTable().getView().getPlayer()).cloneSchema();

    }

    //TODO

    @Override
    public State process(String input) {

        if (input.equalsIgnoreCase("cancel")) return new MainMenuState(this.getGameTable());

        if (actionState == ActionState.CHOOSE) {

            if (times == Times.FIRST) {
                firstSource = CLIPrinter.decodePosition(input);

                if (firstSource == null) {
                    CLIPrinter.printError("Invalid input");
                    return this;
                }

            } else if (times == Times.SECOND) {

                secondSource = CLIPrinter.decodePosition(input);

                if (secondSource == null) {
                    CLIPrinter.printError("Invalid input");
                    return this;
                }

            }

            actionState = ActionState.PLACE;

            return this;

        } else if (actionState == ActionState.PLACE) {

            if (times == Times.FIRST) {

                firstDestination = CLIPrinter.decodePosition(input);

                if (firstDestination == null) {
                    CLIPrinter.printError("Invalid input");
                    return this;
                }

                DiceFace prevDice = playerSchema.removeDiceFace(firstSource);

                if (!playerSchema.isDiceAllowed(firstDestination, prevDice, ignore)) {

                    CLIPrinter.printError("Movement not permitted");

                    this.playerSchema = this.getGameTable().getSchemas(this.getGameTable().getView().getPlayer());
                    actionState = ActionState.CHOOSE;

                    return this;
                }

                playerSchema.setDiceFace(firstDestination, prevDice);

                times = Times.SECOND;
                actionState = ActionState.CHOOSE;

                return this;

            } else if (times == Times.SECOND) {

                secondDestination = CLIPrinter.decodePosition(input);

                if (secondDestination == null) {
                    CLIPrinter.printError("Invalid input");
                    return this;
                }


                DiceFace prevDice = playerSchema.removeDiceFace(secondSource);

                if (!playerSchema.isDiceAllowed(secondDestination, prevDice, ignore)) {

                    CLIPrinter.printError("Movement not permitted");

                    this.playerSchema = this.getGameTable().getSchemas(this.getGameTable().getView().getPlayer());
                    actionState = ActionState.CHOOSE;

                    return this;
                }

                playerSchema.setDiceFace(secondDestination, prevDice);

                if (this.toolName.equals("EglomiseBrush") || this.toolName.equals("CopperReamer"))
                    this.getGameTable().getView().sendEventToController(new MoveDiceEvent(this.getClass().getName(), "", this.getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName(this.toolName), secondSource, secondDestination));


                if (this.toolName.equals("Lathekin"))
                    this.getGameTable().getView().sendEventToController(new DoubleMoveDiceEvent(getClass().getName(), "", this.getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName(toolName), firstSource, firstDestination, secondSource, secondDestination));

            }

        }

        return new MainMenuState(this.getGameTable());

    }

    @Override
    public void render() {

        if (actionState == ActionState.CHOOSE) {

            CLIPrinter.printQuestion("Choose the " + this.times.toString().toLowerCase() + " dice to move: ");
            CLIPrinter.printSchema(playerSchema);
        } else {

            CLIPrinter.printQuestion("Choose the destination: ");

        }

    }

    public enum Times {
        FIRST, SECOND
    }

    public enum ActionState {
        CHOOSE, PLACE
    }


}
