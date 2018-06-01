package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.PlaceRedrawnWithNumberDiceState;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

public class FirmPastaDiluent extends Tool {
    @Override
    public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
        return false;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        DiceActionEvent ev = (DiceActionEvent) event;
        try {
            model.putBackAndRedrawDice(ev.getDicePosition());

            return new PlaceRedrawnWithNumberDiceState(controller, new TurnState(controller, state.isDicePlaced(), true),
                    ev.getPlayerName(), model.getDiceNumberOnDraftBoard() - 1);
        } catch (Exception e) {
            Log.w("W");
            return state;
        }

    }


    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
    // (11) Diluente per pasta salda
}
