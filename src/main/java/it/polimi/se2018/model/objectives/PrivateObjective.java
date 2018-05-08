package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;

public class PrivateObjective extends Objective {

  private GameColor color;

  public PrivateObjective(GameColor color){
    this.color = color;
  }

  @Override
  public int computeScore(Schema schema) {
    return 0;
  }

  public GameColor getColor() {
    return color;
  }
}
