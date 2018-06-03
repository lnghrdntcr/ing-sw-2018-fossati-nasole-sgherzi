package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.viewEvent.PlaceAnotherDiceEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

/**
 * Class for ToolCard 8: Tenaglia a rotelle
 */
public class WheeledPincer extends Tool {
  @Override
  public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
    return model.isFirstTurnInRound();
  }

  @Override
  public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
      PlaceAnotherDiceEvent ev = (PlaceAnotherDiceEvent) event;

      if (model.isDiceAllowed(event.getPlayerName(), ev.getPoint(), model.getDiceFaceByIndex(ev.getDiceFaceIndex()), SchemaCardFace.Ignore.NOTHING)) {
          model.placeDice(event.getPlayerName(), ev.getDiceFaceIndex(), ev.getPoint());
      }else{
          return state;
      }

      model.playerWillDropTurn(event.getPlayerName());
      return new TurnState(controller, state.isDicePlaced(), true);
  }


  @Override
  public ToolCardImmutable getImmutableInstance() {
    return new ToolCardImmutable(this.getClass().getName(), this.getToken());
  }
  // Tenaglia a rotelle
}
