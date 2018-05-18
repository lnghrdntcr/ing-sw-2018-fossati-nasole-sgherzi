package it.polimi.se2018.controller.tool;

import it.polimi.se2018.model.Player;

public class FirmPastaDiluent extends Tool {
  @Override
  public boolean isUsable() {
    return false;
  }

  @Override
  public void use(Player player) {

  }

  @Override
  public Object getImmutableInstance() {
    return null;
  }
  // Diluente per pasta salda
}
