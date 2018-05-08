package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.Schema;

public abstract class Objective {
  public abstract int computeScore(Schema schema);
}
