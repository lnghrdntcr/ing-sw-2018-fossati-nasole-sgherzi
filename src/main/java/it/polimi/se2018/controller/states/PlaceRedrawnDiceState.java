package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceEvent;
import it.polimi.se2018.controller.controllerEvent.LogEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Utils;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;
import it.polimi.se2018.view.viewEvent.PlayerDisconnectedEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

/**
 * A state that handle the intermediate state after using "Firm Pasta Brush" tool card.
 */
public class PlaceRedrawnDiceState extends State {
    private final State oldState;
    private final DiceFace redrawnDiceFace;

    public PlaceRedrawnDiceState(Controller controller, GameTableMultiplayer model, State oldState, DiceFace redrawnDiceFace, String playerName, int diceNumberOnDraftBoard) {
        super(controller, model);
        this.oldState = oldState;
        this.redrawnDiceFace = redrawnDiceFace;
        getController().dispatchEvent(new AskPlaceRedrawDiceEvent(getClass().getCanonicalName(), playerName, playerName, diceNumberOnDraftBoard));
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
        //TODO: check if the player is the current player
        try {
            PlaceAnotherDiceEvent ev = (PlaceAnotherDiceEvent) event;
            //checks if user is placing the redrawn face
            if (getModel().getDiceFaceByIndex(ev.getDiceFaceIndex()).equals(redrawnDiceFace)) {
                DiceFace df = getModel().getDiceFaceByIndex(ev.getDiceFaceIndex());
                if (getModel().isDiceAllowed(ev.getPlayerName(), ev.getPoint(), redrawnDiceFace, SchemaCardFace.Ignore.NOTHING)) {
                    getModel().placeDice(ev.getPlayerName(), ev.getDiceFaceIndex(), ev.getPoint());



                    StringBuilder message = new StringBuilder(event.getPlayerName() + " has used " + getModel().getToolCardByPosition(event.getToolCardIndex()).getName());
                    message.append("The ");
                    message.append(Utils.decodeDice(df) + "[" + Utils.decodeCardinalNumber(ev.getDiceFaceIndex() + 1) + "dice] ");
                    message.append(" was placed in position");
                    message.append(Utils.decodePosition(ev.getPoint()));

                    getController().dispatchEvent(new LogEvent(this.getClass().getName(), event.getPlayerName(), "", message.toString()));

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
     * Handle the timeout of the current player
     *
     * @return the new state of the game
     */
    @Override
    public State handleUserTimeOutEvent() {
        Log.d(getClass().getCanonicalName() + " handling UserTimeoutEvent");
        //if the user timed out, simply do not let him place any dice
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
