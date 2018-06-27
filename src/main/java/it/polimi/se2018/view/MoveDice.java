package it.polimi.se2018.view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.viewEvent.DoubleMoveDiceEvent;
import it.polimi.se2018.view.viewEvent.DoubleMoveOfColorDiceEvent;
import it.polimi.se2018.view.viewEvent.MoveDiceEvent;

import java.awt.*;

public abstract class MoveDice extends State {
    private Point firstSource;
    private Point secondSource;
    private Point firstDestination;
    private Point secondDestination;
    SchemaCardFace.Ignore ignore;
    String toolName;
    private Times times;
    private final GameColor color;
    private ActionState actionState;


    private Schema playerSchema;

    public MoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times) {
        this(gameTable, ignore, toolName, times, null);
    }

    public MoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times, GameColor color) {

        super(gameTable);
        this.ignore = ignore;
        this.toolName = toolName;
        this.times = times;
        this.color = color;
        actionState = ActionState.CHOOSE;

        playerSchema = this.getGameTable().getSchema(this.getGameTable().getView().getPlayer()).cloneSchema();

    }

    public void processFirstSource(Point firstSource){
        this.firstSource=firstSource;
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

        actionState = ActionState.PLACE;
    }

    public void processSecondSource(Point secondSource){
        this.secondSource=secondSource;
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

        actionState = ActionState.PLACE;
    }

    public void processFirstDestination(Point firstDestination){
        this.firstDestination=firstDestination;
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
    }

    public void processSecondDestination(Point secondDestination){
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

        if (this.toolName.equals("ManualCutter")) {
            this.getGameTable().getView().sendEventToController(new DoubleMoveOfColorDiceEvent(getClass().getName(), "", getGameTable().getView().getPlayer(), getGameTable().getToolIndexByName(toolName), firstSource, firstDestination, secondSource, secondDestination, color));
        }

        getGameTable().setState(new CLIMainMenuState(getGameTable()));

    }


    public Times getTimes() {
        return times;
    }

    public ActionState getActionState() {
        return actionState;
    }

    public Schema getTemporaryPlayerSchema() {
        return playerSchema;
    }

    public enum Times {
        FIRST, SECOND
    }

    public enum ActionState {
        CHOOSE, PLACE
    }


}
