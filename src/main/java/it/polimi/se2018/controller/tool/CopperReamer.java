package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.CopperReamerEvent;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;

public class CopperReamer extends Tool {
    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        CopperReamerEvent e = (CopperReamerEvent) event;
        //TODO check lastMove field with Fossafame
        model.moveDice(model.getCurrentPlayerName(), e.getSource(), e.getDestination(), true);
        return new TurnState(controller, model.getCurrentPlayerName(), state.isDicePlaced(), true);
    }


    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
    //(3) Alesatore per lamina di rame
}
