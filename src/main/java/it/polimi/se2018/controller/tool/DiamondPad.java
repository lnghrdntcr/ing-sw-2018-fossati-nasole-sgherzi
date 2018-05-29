package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;

public class DiamondPad extends Tool {
  @Override
  public boolean isUsable() {
    return false;
  }

  @Override
  public State use(Controller controller, GameTableMultiplayer model, State state) {
    return null;
  }


  @Override
  public ToolCardImmutable getImmutableInstance() {
    return new ToolCardImmutable(this.getClass().getName(), this.getToken());
  }
  // Tampone diamantato
}
