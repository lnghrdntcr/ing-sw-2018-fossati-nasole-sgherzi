package it.polimi.se2018.model.schema_card;

public class NumberRestriction extends CellRestriction  {

  private int number;

  public NumberRestriction(int number){
    this.number = number;
  }

  @Override
  public boolean isDiceAllowed() {
    return false;
  }

  public int getNumber() {
    return this.number;
  }
}
