package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceEvent;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceWithNumberSelectionEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceSelectingNumberEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

/**
 * A state that handle the intermediate state after using "Firm Pasta Diluent" tool card.
 */
public class PlaceRedrawnWithNumberDiceState extends State {
    private final State oldState;

    public PlaceRedrawnWithNumberDiceState(Controller controller, GameTableMultiplayer model, State oldState, String playerName, int diceNumberOnDraftBoard) {
        super(controller, model);
        this.oldState = oldState;
        getController().dispatchEvent(new AskPlaceRedrawDiceWithNumberSelectionEvent(getClass().getCanonicalName(), playerName, diceNumberOnDraftBoard));
    }

    @Override
    public State handleToolcardEvent(UseToolcardEvent event) {
        try{
            PlaceAnotherDiceSelectingNumberEvent ev = (PlaceAnotherDiceSelectingNumberEvent) event;
            //checks if user is placing the redrawn face
            if(ev.getDiceFaceIndex()==getModel().getDiceNumberOnDraftBoard()-1){
                getModel().changeDiceNumber(ev.getDiceFaceIndex(), ev.getNumber());
                if(getModel().isDiceAllowed(ev.getPlayerName(), ev.getPoint(), getModel().getDiceFaceByIndex(getModel().getDiceNumberOnDraftBoard()-1), SchemaCardFace.Ignore.NOTHING )){
                    getModel().placeDice(ev.getPlayerName(), ev.getDiceFaceIndex(), ev.getPoint());
                    return oldState;
                }else{
                    Log.w(getClass().getCanonicalName()+": the dice face can't be placed here!");
                    return this;
                }
            }else{
                Log.w(getClass().getCanonicalName()+": only the redrawn face can be placed");
                return this;
            }

        }catch (Exception e){
            Log.w(getClass().getCanonicalName()+": there was an exception: "+e.getMessage());
            return this;
        }

    }

    @Override
    public State handleUserTimeOutEvent() {
        //If the user disconnected or timed out, simply return, leaving the dice as it is
        return oldState;
    }
}
