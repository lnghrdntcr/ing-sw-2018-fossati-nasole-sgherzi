package it.polimi.se2018.model.schema_card;

public class NoRestriction extends CellRestriction {
  @Override
  public boolean isDiceAllowed() {
    return true;
  }
}
