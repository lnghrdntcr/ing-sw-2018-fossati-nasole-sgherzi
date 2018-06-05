package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.ChangeDiceNumberEvent;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

/**
 * Class for ToolCard 1: Pinza sgrossatrice
 */
public class RoughingNipper extends Tool {
    @Override
    public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
        return true;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        ChangeDiceNumberEvent ev = (ChangeDiceNumberEvent) event;

        try {
            model.increaseDecreaseDice(ev.getDicePosition(), ev.getDicePosition());
            return new TurnState(controller, model, state.isDicePlaced(), true);
        } catch (Exception e){
            Log.w("Unable to flip the dice: "+e.getMessage());
            return state;
        }

    }

    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
    // (1) Pinza sgrossatrice
}
