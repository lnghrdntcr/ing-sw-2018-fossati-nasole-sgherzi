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
import it.polimi.se2018.view.viewEvent.DoubleMoveDiceEvent;

/**
 * Class for ToolCard 4: Lathekin
 */
public class Lathekin extends Tool {
    @Override
    public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
        return true;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        DoubleMoveDiceEvent ev = (DoubleMoveDiceEvent) event;
        Schema tempSchema = model.getPlayerSchemaCopy(ev.getPlayerName());
        DiceFace tempDice = tempSchema.removeDiceFace(ev.getSource(0));

        if (tempSchema.isDiceAllowed(ev.getDestination(0), tempDice, SchemaCardFace.Ignore.NOTHING)) {
            tempSchema.setDiceFace(ev.getDestination(0), tempDice);
            tempDice=tempSchema.removeDiceFace(ev.getSource(1));
            if(tempSchema.isDiceAllowed(ev.getDestination(1), tempDice, SchemaCardFace.Ignore.NOTHING)){
                model.moveDice(ev.getPlayerName(), ev.getSource(0), ev.getDestination(0), false);
                model.moveDice(ev.getPlayerName(), ev.getSource(1), ev.getDestination(1), true);
                return new TurnState(controller, model, state.isDicePlaced(), true);
            }else{
                Log.w(getClass().getCanonicalName()+": second move not allowed!");
                return state;
            }

        }
        else{
            Log.w(getClass().getCanonicalName()+": first move no allowed!");
            return state;
        }

    }


    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
    // (4) Lathekin
}
