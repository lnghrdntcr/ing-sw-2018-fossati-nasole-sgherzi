package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.MoveDiceEvent;

public class CorkRow extends Tool {
  @Override
  public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
    return true;
  }

  @Override
  public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
    MoveDiceEvent e = (MoveDiceEvent) event;
    String name = model.getCurrentPlayerName();
    if (model.isAloneDiceAllowed(name, e.getDestination(), model.getPlayerDiceFace(e.getPlayerName(), e.getSource()), SchemaCardFace.Ignore.NOTHING)) {
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
  //(9) Riga in sughero
}
