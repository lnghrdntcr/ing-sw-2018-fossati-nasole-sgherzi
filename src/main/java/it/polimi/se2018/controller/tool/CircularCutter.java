package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.viewEvent.SwapDiceFaceWithTurnHolderEvent;

/**
 * Class for ToolCard 5: Taglierina circolare
 */
public class CircularCutter extends Tool {
    @Override
    public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
        return true;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        SwapDiceFaceWithTurnHolderEvent ev = (SwapDiceFaceWithTurnHolderEvent) event;
        try{
            model.swapDraftDiceWithHolder(ev.getDraftBoardIndex(), ev.getTurn(), ev.getIndexInTurn());
            return new TurnState(controller, state.isDicePlaced(), true);
        }catch (Exception e){

            return state;
        }
    }

    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
}
