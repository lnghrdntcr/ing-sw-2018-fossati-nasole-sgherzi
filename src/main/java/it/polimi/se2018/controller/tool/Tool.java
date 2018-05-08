package it.polimi.se2018.controller.tool;

import it.polimi.se2018.model.Player;

public abstract class Tool {

  private int token;

  public abstract boolean isUsable();

  public abstract void use(Player player);

  public int getToken() {
    return token;
  }

  public void addToken(int tokenAdded) {
    this.token += tokenAdded;
  }
}
