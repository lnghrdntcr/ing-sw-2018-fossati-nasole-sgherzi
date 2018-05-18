package it.polimi.se2018.controller.tool;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;

public class CopperReamer extends Tool{
  @Override
  public boolean isUsable() {
    return false;
  }

  @Override
  public void use(Player player) {

  }

  @Override
  public ToolCardImmutable getImmutableInstance() {
    return new ToolCardImmutable(this.getClass().getName(), this.getToken());
  }
  // Alesatore per lamina di rame
}
