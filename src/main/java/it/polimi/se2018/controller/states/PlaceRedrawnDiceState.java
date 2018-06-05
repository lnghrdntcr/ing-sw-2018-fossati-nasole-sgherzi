package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

/**
 * A state that handle the intermediate state after using {@link it.polimi.se2018.controller.tool.FirmPastaBrush}
 */
public class PlaceRedrawnDiceState extends State {
    private final State oldState;
    private final DiceFace redrawnDiceFace;

    public PlaceRedrawnDiceState(Controller controller, GameTableMultiplayer model, State oldState, DiceFace redrawnDiceFace, String playerName, int diceNumberOnDraftBoard) {
        super(controller, model);
        this.oldState = oldState;
        this.redrawnDiceFace = redrawnDiceFace;
        getController().dispatchEvent(new AskPlaceRedrawDiceEvent(getClass().getCanonicalName(), playerName, diceNumberOnDraftBoard));
    }

    @Override
    public State handleToolcardEvent(UseToolcardEvent event) {
        try{
            PlaceAnotherDiceEvent ev = (PlaceAnotherDiceEvent) event;
            //checks if user is placing the redrawn face
            if(getModel().getDiceFaceByIndex(ev.getDiceFaceIndex()).equals(redrawnDiceFace)){
                if(getModel().isDiceAllowed(ev.getPlayerName(), ev.getPoint(), redrawnDiceFace, SchemaCardFace.Ignore.NOTHING )){
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
        //if the user timed out, simply do not let him place any dice
        return oldState;
    }
}
