package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.Schema;

public abstract class PublicObjective extends Objective {

  private int points;

  public PublicObjective(int points) {
    this.points = points;
  }

  public int getPoint() {
    return points;
  }
}
