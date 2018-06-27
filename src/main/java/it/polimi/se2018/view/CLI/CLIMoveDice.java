package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.viewEvent.DoubleMoveDiceEvent;
import it.polimi.se2018.view.viewEvent.DoubleMoveOfColorDiceEvent;
import it.polimi.se2018.view.viewEvent.MoveDiceEvent;

import java.awt.*;

public class CLIMoveDice extends State {

    SchemaCardFace.Ignore ignore;
    String toolName;
    private Times times;
    private final GameColor color;
    private ActionState actionState;

    private Point firstSource;
    private Point secondSource;
    private Point firstDestination;
    private Point secondDestination;
    private Schema playerSchema;

    public CLIMoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times) {
        this(gameTable, ignore, toolName, times, null);
    }

    public CLIMoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times, GameColor color) {

        super(gameTable);
        this.ignore = ignore;
        this.toolName = toolName;
        this.times = times;
        this.color = color;
        actionState = ActionState.CHOOSE;

        playerSchema = this.getGameTable().getSchema(this.getGameTable().getView().getPlayer()).cloneSchema();

    }


    //TODO

    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")){
            getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
            return;
        }

        if (actionState == ActionState.CHOOSE) {

            if (times == Times.FIRST) {
                firstSource = CLIPrinter.decodePosition(input);

                if (firstSource == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                if (playerSchema.getDiceFace(firstSource) == null) {
                    CLIPrinter.printError("The cell is empty!");
                    getGameTable().setState(this);
                    return;
                }

                if (color != null && !playerSchema.getDiceFace(firstSource).getColor().equals(color)) {
                    CLIPrinter.printError("You cannot move a dice of this color!");
                    getGameTable().setState(this);
                    return;
                }

            } else if (times == Times.SECOND) {

                secondSource = CLIPrinter.decodePosition(input);

                if (secondSource == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                if (playerSchema.getDiceFace(secondSource) == null) {
                    CLIPrinter.printError("The cell is empty!");
                    getGameTable().setState(this);
                    return;
                }

                if (color != null && !playerSchema.getDiceFace(secondSource).getColor().equals(color)) {
                    CLIPrinter.printError("You cannot move a dice of this color!");
                    getGameTable().setState(this);
                    return;
                }

            }

            actionState = ActionState.PLACE;

            getGameTable().setState(this);

            return;
        } else if (actionState == ActionState.PLACE) {

            if (times == Times.FIRST) {

                firstDestination = CLIPrinter.decodePosition(input);

                if (firstDestination == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }


                if (playerSchema.getDiceFace(firstDestination) != null) {
                    CLIPrinter.printError("The cell not empty!");
                    getGameTable().setState(this);
                    return;
                }

                DiceFace prevDice = playerSchema.removeDiceFace(firstSource);

                if (!playerSchema.isDiceAllowed(firstDestination, prevDice, ignore)) {

                    CLIPrinter.printError("Movement not permitted");

                    this.playerSchema = this.getGameTable().getSchema(this.getGameTable().getView().getPlayer());
                    actionState = ActionState.CHOOSE;

                    getGameTable().setState(this);
                    return;
                }

                playerSchema.setDiceFace(firstDestination, prevDice);

                times = Times.SECOND;
                actionState = ActionState.CHOOSE;

                getGameTable().setState(this);
                return;

            } else if (times == Times.SECOND) {

                secondDestination = CLIPrinter.decodePosition(input);

                if (secondDestination == null) {
                    CLIPrinter.printError("Invalid input");
                    getGameTable().setState(this);
                    return;
                }

                if (playerSchema.getDiceFace(secondDestination) != null) {
                    CLIPrinter.printError("The cell not empty!");
                    getGameTable().setState(this);
                    return;
                }

                DiceFace prevDice = playerSchema.removeDiceFace(secondSource);

                if (!playerSchema.isDiceAllowed(secondDestination, prevDice, ignore)) {

                    CLIPrinter.printError("Movement not permitted");

                    this.playerSchema = this.getGameTable().getSchema(this.getGameTable().getView().getPlayer());
                    actionState = ActionState.CHOOSE;

                    getGameTable().setState(this);
                    return;
                }

                playerSchema.setDiceFace(secondDestination, prevDice);

                if (this.toolName.equals("EglomiseBrush") || this.toolName.equals("CopperReamer"))
                    this.getGameTable().getView().sendEventToController(new MoveDiceEvent(this.getClass().getName(), "", this.getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName(this.toolName), secondSource, secondDestination));


                if (this.toolName.equals("Lathekin"))
                    this.getGameTable().getView().sendEventToController(new DoubleMoveDiceEvent(getClass().getName(), "", this.getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName(toolName), firstSource, firstDestination, secondSource, secondDestination));

                if(this.toolName.equals("ManualCutter")){
                    this.getGameTable().getView().sendEventToController(new DoubleMoveOfColorDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName(toolName), firstSource, firstDestination, secondSource, secondDestination, color));
                }
            }

        }

        getGameTable().setState(new CLIMainMenuState(getGameTable()));
        return;

    }

    @Override
    public void unrealize() {

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
