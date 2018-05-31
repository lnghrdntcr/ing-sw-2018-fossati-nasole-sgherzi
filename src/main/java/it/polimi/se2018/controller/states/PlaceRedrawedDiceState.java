package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawedDiceEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;

public class PlaceRedrawedDiceState extends State {
    private final State oldState;
    private final DiceFace redrawnDiceFace;

    public PlaceRedrawedDiceState(Controller controller, State oldState, DiceFace redrawnDiceFace, String playerName, int diceNumberOnDraftBoard) {
        super(controller);
        this.oldState = oldState;
        this.redrawnDiceFace = redrawnDiceFace;
        getController().dispatchEvent(new AskPlaceRedrawedDiceEvent(getClass().getCanonicalName(), playerName, diceNumberOnDraftBoard));
    }

    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {
        try{
            PlaceAnotherDiceEvent ev = (PlaceAnotherDiceEvent) event;
            //checks if user is placing the redrawn face
            if(model.getDiceFaceByIndex(ev.getDiceFaceIndex()).equals(redrawnDiceFace)){
                if(model.isDiceAllowed(ev.getPlayerName(), ev.getPoint(), redrawnDiceFace, SchemaCardFace.Ignore.NOTHING )){
                    model.placeDice(ev.getPlayerName(), ev.getDiceFaceIndex(), ev.getPoint());
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
}
