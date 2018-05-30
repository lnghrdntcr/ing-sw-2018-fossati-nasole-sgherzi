package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

/**
 * The state that handles the state of the turn.
 * @since 29/05/2018
 */
public class TurnState extends State {

    private boolean hasPlacedDice;
    private boolean hasUsedToolcard;

    public TurnState(Controller controller, boolean hasPlacedDice, boolean hasUsedToolcard) {
        super(controller);
        this.hasPlacedDice = hasPlacedDice;
        this.hasUsedToolcard = hasUsedToolcard;
    }

    /**
     * Handles the incoming event and dispatches actions to handle it.
     * @param event The event to be handled
     * @param model The model.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event is null.
     */
    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {
        if (event == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if (event instanceof UseToolcardEvent) return this.handleToolcardUse((UseToolcardEvent) event, model);
        if (event instanceof PlaceDiceEvent) return this.handleDicePlacing((PlaceDiceEvent) event, model);
        if (event instanceof EndTurnEvent) return this.handleTurnEnding((EndTurnEvent) event, model);

        return new TurnState(this.getController(), this.hasPlacedDice, this.hasUsedToolcard);

    }

    /**
     * Handles the incoming UseToolcardEvent.
     * @param event The event to be handled.
     * @param model The model.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event is null.
     */
    private State handleToolcardUse(UseToolcardEvent event, GameTableMultiplayer model) {

        Tool tool = model.getToolCardByPosition(event.getToolCardIndex());
        int playerToken = model.getPlayerToken(event.getPlayerName());

        if (!model.getCurrentPlayerName().equals(event.getPlayerName())) Log.w("Only current player can use a toolcard");

        if (!tool.isUsable(model, this)) Log.i(tool.getClass().getName() + "not usable in this turn.");

        if (playerToken < tool.getNeededTokens()) {
            Log.i(
                    event.getPlayerName()
                            + " cannot use the " + tool.getClass().getName() + " toolcard:\n "
                            + "Tokens needed:\t" + tool.getNeededTokens()
                            + "\n Actual tokens:\t" + playerToken
            );
            return this;
        } else {
            return tool.use(getController(), model, this, event);
        }

    }

    /**
     * Handles the incoming PlaceDiceEvent.
     * @param event The event to be handled.
     * @param model The model.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event or model is null.
     */
    private State handleDicePlacing(PlaceDiceEvent event, GameTableMultiplayer model) {

        if(event == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if(model == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null");

        if (this.hasPlacedDice) {
            Log.w(event.getPlayerName() + " has already placed a dice.");
            return this;
        }

        if (!model.getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + ": Only the current player can place a dice!");
            return this;
        }
        if (model.isDiceAllowed(
                event.getPlayerName(),
                event.getPoint(),
                model.getDiceFaceByIndex(
                    event.getDiceFaceIndex()
                ),
                SchemaCardFace.Ignore.NOTHING))
        {
            model.placeDice(event.getPlayerName(), event.getDiceFaceIndex(), event.getPoint());
        }else{
            return this;
        }

        return new TurnState(this.getController(), true, this.hasUsedToolcard);

    }

    /**
     * Handles the incoming EndTurnEvent.
     * @param event The event to be handled.
     * @param model The model.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event or model is null.
     */
    private State handleTurnEnding(EndTurnEvent event, GameTableMultiplayer model) {

        if(event == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if(model == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null");

        if (!model.getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + "Only the current player can end its turn!");
        } else {
            model.nextTurn();
        }

        return new TurnState(this.getController(), false, false);

    }

    public boolean isDicePlaced() {
        return hasPlacedDice;
    }

}
