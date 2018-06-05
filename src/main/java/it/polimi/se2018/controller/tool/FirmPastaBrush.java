package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.PlaceRedrawnDiceState;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

/**
 * Class for ToolCard 6: Pennello per pasta salda
 */
public class FirmPastaBrush extends Tool {
    @Override
    public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
        return false;
    }

    @Override
    public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
        try {
            DiceActionEvent ev = (DiceActionEvent) event;
            DiceFace redrawed = model.redrawDice(ev.getDicePosition());

            if (model.getPlayerSchemaCopy(ev.getPlayerName()).isDiceAllowedSomewhere(redrawed, SchemaCardFace.Ignore.NOTHING)) {
                //the diceface can be placed
                return new PlaceRedrawnDiceState(controller, model, new TurnState(controller, model, state.isDicePlaced(), true), redrawed, ev.getPlayerName(), model.getDiceNumberOnDraftBoard()-1);
            } else {
                //the diceface cannot be placed
                return new TurnState(controller, model, state.isDicePlaced(), true);
            }

        } catch (Exception e) {
            Log.w(getClass().getCanonicalName() + ": cannot use FirmPastaBrush: " + e.getMessage());
            return state;
        }
    }


    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(this.getClass().getName(), this.getToken());
    }
    // (6) Pennello per pasta salda
}
