package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.Schema;

public class DarkShades extends PublicObjective {
  public DarkShades(int points) {
    super(points);
  }

  @Override
  public int computeScore(Schema schema) {
    return 0;
  }
}
