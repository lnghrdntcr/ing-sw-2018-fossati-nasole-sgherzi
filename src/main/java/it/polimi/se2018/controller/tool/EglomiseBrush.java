package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.MoveDiceEvent;

public class EglomiseBrush extends Tool {
    @Override
    public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
        return false;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        MoveDiceEvent e = (MoveDiceEvent) event;
        String name = model.getCurrentPlayerName();
        Schema tempSchema = model.getPlayerSchemaCopy(e.getPlayerName());
        DiceFace tempDiceFace = tempSchema.removeDiceFace(e.getSource());
        if (tempSchema.isDiceAllowed(e.getDestination(), tempDiceFace,SchemaCardFace.Ignore.COLOR)){
            model.useTokenOnToolcard(event.getPlayerName(), this);
            model.moveDice(name, e.getSource(), e.getDestination(), true);
            return new TurnState(controller, state.isDicePlaced(), true);
        }
    else{
            Log.w("Destination not allowed");
            return state;
        }
    }


    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
    // (2) Pennello per Eglomise
}
