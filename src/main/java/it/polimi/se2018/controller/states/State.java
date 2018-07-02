package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.*;

/**
 * A generic State used by the controller to handle a player event
 */
public abstract class State {
    private Controller controller;
    private GameTableMultiplayer model;

    public State(Controller controller, GameTableMultiplayer model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * Handles an event generated by a player
     *
     * @param event the event to handle
     * @param model the model to modify
     * @return the new State for the controller
     */
    public final State handleEvent(ViewEvent event, GameTableMultiplayer model) {
        return event.visit(this);
    }

    public abstract void syncPlayer(String playerName);

    /**
     * Handle the usage of a toolcard (the default implementation does nothing)
     *
     * @param event the event that has triggered this method
     * @return the new state of the game
     */
    public State handleToolcardEvent(UseToolcardEvent event) {
        Log.i(getClass().getCanonicalName() + ": cannot handle ToolcardEvent here!");
        return this;
    }

    /**
     * Handle the end of a turn (the default implementation does nothing)
     *
     * @param event the event that has triggered this method
     * @return the new state of the game
     */
    public State handleEndTurnEvent(EndTurnEvent event) {
        Log.i(getClass().getCanonicalName() + ": cannot handle EndTurnEvent here!");
        return this;
    }

    /**
     * Handle the placement of a dice (the default implementation does nothing)
     *
     * @param event the event that has triggered this method
     * @return the new state of the game
     */
    public State handlePlaceDiceEvent(PlaceDiceEvent event) {
        Log.i(getClass().getCanonicalName() + ": cannot handle PlaceDiceEvent here!");
        return this;
    }

    /**
     * Handle the selection of a schemacard (the default implementation does nothing)
     *
     * @param event the event that has triggered this method
     * @return the new state of the game
     */
    public State handleSchemaCardSelectedEvent(SchemaCardSelectedEvent event) {
        Log.i(getClass().getCanonicalName() + ": cannot handle SchemaCardSelectedEvent here!");
        return this;
    }

    /**
     * Handle the timeout of a player (the default implementation does nothing)
     *
     * @return the new state of the game
     */
    public State handleUserTimeOutEvent() {
        Log.i(this.getClass().getCanonicalName() + ": cannot handle PlayerTimeoutEvent here");
        return this;
    }


    /**
     * @return the current controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * @return the current model
     */
    public GameTableMultiplayer getModel() {
        return model;
    }

    public State handlePlayerDisconnected(PlayerDisconnectedEvent playerDisconnectedEvent) {
        return this;
    }

    public State handleUserCancelEvent() {
        Log.i(this.getClass().getCanonicalName() + ": Cannot handle UserCancelEvent here.");
        return this;
    }
}
