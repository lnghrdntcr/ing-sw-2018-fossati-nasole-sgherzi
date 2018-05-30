package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;

public class WheeledPincer extends Tool {
  @Override
  public boolean isUsable(GameTableMultiplayer model, TurnState currentState) {
    return false;
  }

  @Override
  public State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event) {
    return null;
  }


  @Override
  public ToolCardImmutable getImmutableInstance() {
    return new ToolCardImmutable(this.getClass().getName(), this.getToken());
  }
  // Tenaglia a rotelle
}
