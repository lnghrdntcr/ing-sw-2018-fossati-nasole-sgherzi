package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.*;

/**
 * The state that handles the state of the turn.
 * @since 29/05/2018
 */
public class TurnState extends State {

    private boolean hasPlacedDice;
    private boolean hasUsedToolcard;

    public TurnState(Controller controller, GameTableMultiplayer model, boolean hasPlacedDice, boolean hasUsedToolcard) {
        super(controller, model);
        this.hasPlacedDice = hasPlacedDice;
        this.hasUsedToolcard = hasUsedToolcard;
    }

    /**
     * Handles the incoming event and dispatches actions to handle it.
     * @param event The event to be handled
     * @return A new Turn state.
     * @throws IllegalArgumentException If event is null.
     */
    /*@Override
    public State handleEvent(Event event, GameTableMultiplayer model) {
        if (event == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if (event instanceof UseToolcardEvent) return this.handleToolcardUse((UseToolcardEvent) event, model);
        if (event instanceof PlaceDiceEvent) return this.handleDicePlacing((PlaceDiceEvent) event, model);
        if (event instanceof EndTurnEvent) return this.handleTurnEnding((EndTurnEvent) event, model);
        if (event instanceof PlayerTimeoutEvent) return this.handlePlayerTimeout((PlayerTimeoutEvent) event, model);

        return new TurnState(this.getController(), getModel(), this.hasPlacedDice, this.hasUsedToolcard);

    }*/

    /**
     * Handles the incoming UseToolcardEvent.
     * @param event The event to be handled.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event is null.
     */
    @Override
    public State handleToolcardEvent(UseToolcardEvent event) {

        if(event == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");

        if (!getModel().getCurrentPlayerName().equals(event.getPlayerName())) Log.w("Only current player can use a toolcard");

        Tool tool = getModel().getToolCardByPosition(event.getToolCardIndex());
        int playerToken = getModel().getPlayerToken(event.getPlayerName());

        if (!tool.isUsable(getModel(), this)) Log.i(tool.getClass().getName() + "not usable in this turn.");

        if (playerToken < tool.getNeededTokens()) {
            Log.i(
                    event.getPlayerName()
                            + " cannot use the " + tool.getClass().getName() + " toolcard:\n "
                            + "Tokens needed:\t" + tool.getNeededTokens()
                            + "\n Actual tokens:\t" + playerToken
            );
            return this;
        } else {
            return tool.use(getController(), getModel(), this, event);
        }

    }

    /**
     * Handles the incoming PlaceDiceEvent.
     * @param event The event to be handled.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event or model is null.
     */
    @Override
    public State handlePlaceDiceEvent(PlaceDiceEvent event) {

        if(event == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if(getModel() == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null");

        if (this.hasPlacedDice) {
            Log.w(event.getPlayerName() + " has already placed a dice.");
            return this;
        }

        if (!getModel().getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + ": Only the current player can place a dice!");
            return this;
        }
        if (getModel().isDiceAllowed(
                event.getPlayerName(),
                event.getPoint(),
                getModel().getDiceFaceByIndex(
                    event.getDiceFaceIndex()
                ),
                SchemaCardFace.Ignore.NOTHING))
        {
            getModel().placeDice(event.getPlayerName(), event.getDiceFaceIndex(), event.getPoint());
        }else{
            return this;
        }

        return new TurnState(this.getController(),getModel(), true, this.hasUsedToolcard);

    }

    @Override
    public State handleUserTimeOutEvent() {
        return this.handleEndTurnEvent(new EndTurnEvent("UserTimeout", this.getModel().getCurrentPlayerName()));
    }

    /**
     * Handles the incoming EndTurnEvent.
     * @param event The event to be handled.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event or model is null.
     */
    @Override
    public State handleEndTurnEvent(EndTurnEvent event) {

        if(event == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if(getModel() == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null");

        if (!getModel().getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + "Only the current player can end its turn!");
        } else {
            if(getModel().hasNextTurn()){
                getModel().nextTurn();
                return new TurnState(this.getController(), getModel(),false, false);
            } else {
                return new GameEndState(this.getController(), getModel());
            }

        }

        return this;

    }

    public boolean isDicePlaced() {
        return this.hasPlacedDice;
    }

    public boolean isToolcardUsed(){
        return this.hasUsedToolcard;
    }

}
