package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceWithNumberSelectionEvent;
import it.polimi.se2018.controller.controllerEvent.LogEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceSelectingNumberEvent;
import it.polimi.se2018.view.viewEvent.PlayerDisconnectedEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

/**
 * A state that handle the intermediate state after using "Firm Pasta Diluent" tool card.
 */
public class PlaceRedrawnWithNumberDiceState extends State {

    private final State oldState;

    public PlaceRedrawnWithNumberDiceState(Controller controller, GameTableMultiplayer model, State oldState, String playerName, int diceNumberOnDraftBoard) {
        super(controller, model);
        this.oldState = oldState;
        getController().dispatchEvent(new AskPlaceRedrawDiceWithNumberSelectionEvent(getClass().getCanonicalName(), playerName, playerName, diceNumberOnDraftBoard));
    }

    /**
     * Performs all the necessary action to sync the current game state and a reconnected user
     * @param playerName the player to resync
     */

    @Override
    public void syncPlayer(String playerName) {
        this.oldState.syncPlayer(playerName);
    }

    /**
     * Handle the usage of a specific toolcard
     *
     * @param event the event that has triggered this method
     * @return the new state of the game
     */
    @Override
    public State handleToolcardEvent(UseToolcardEvent event) {
        Log.d(getClass().getCanonicalName() + " handling ToolcardEvent");
        try {
            PlaceAnotherDiceSelectingNumberEvent ev = (PlaceAnotherDiceSelectingNumberEvent) event;
            //checks if user is placing the redrawn face
            if (ev.getDiceFaceIndex() == getModel().getDiceNumberOnDraftBoard() - 1) {
                getModel().changeDiceNumber(ev.getDiceFaceIndex(), ev.getNumber());
                if (getModel().isDiceAllowed(ev.getPlayerName(), ev.getPoint(), getModel().getDiceFaceByIndex(getModel().getDiceNumberOnDraftBoard() - 1), SchemaCardFace.Ignore.NOTHING)) {
                    getModel().placeDice(ev.getPlayerName(), ev.getDiceFaceIndex(), ev.getPoint());

                    getController().dispatchEvent(new LogEvent(this.getClass().getName(), event.getPlayerName(), "", event.getPlayerName() + " has used " + getModel().getToolCardByPosition(event.getToolCardIndex()).getName()));

                    return oldState;
                } else {
                    Log.w(getClass().getCanonicalName() + ": the dice face can't be placed here!");
                    return this;
                }
            } else {
                Log.w(getClass().getCanonicalName() + ": only the redrawn face can be placed");
                return this;
            }

        } catch (Exception e) {
            Log.w(getClass().getCanonicalName() + ": there was an exception: " + e.getMessage());
            return this;
        }

    }

    /**
     * Handle the cancellation of the action from a toolcard.
     *
     * @return the old state.
     */
    @Override
    public State handleUserCancelEvent() {
        Log.d("User Cancelled current action");
        return oldState;
    }

    /**
     * Handle the current user timeout
     *
     * @return the new state of the game
     */
    @Override
    public State handleUserTimeOutEvent() {
        Log.d(getClass().getCanonicalName() + " handling UserTimeoutEvent");
        //If the user disconnected or timed out, simply return, leaving the dice as it is
        return oldState;
    }

    @Override
    public State handlePlayerDisconnected(PlayerDisconnectedEvent playerDisconnectedEvent) {
        if (playerDisconnectedEvent.getPlayerName().equals(getModel().getCurrentPlayerName())) {
            return oldState.handlePlayerDisconnected(playerDisconnectedEvent);
        }
        return super.handlePlayerDisconnected(playerDisconnectedEvent);
    }
}
