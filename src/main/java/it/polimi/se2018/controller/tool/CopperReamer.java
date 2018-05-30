package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.CopperReamerEvent;

public class CopperReamer extends Tool {
    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        CopperReamerEvent e = (CopperReamerEvent) event;
        String name = model.getCurrentPlayerName();
        if (model.isDiceAllowed(name, e.getDestination(), e.getDiceFace(), SchemaCardFace.Ignore.NUMBER)) {
            model.moveDice(name, e.getSource(), e.getDestination(), true);
            model.useTokenOnToolcard(event.getPlayerName(), this);
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
    //(3) Alesatore per lamina di rame
}
