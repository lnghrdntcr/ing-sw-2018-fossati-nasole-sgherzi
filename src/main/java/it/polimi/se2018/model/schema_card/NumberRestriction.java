package it.polimi.se2018.model.schema_card;

public class NumberRestriction extends CellRestriction  {

  private int number;

  NumberRestriction(int number){

  }

  @Override
  public boolean isDiceAllowed() {
    return false;
  }
}
