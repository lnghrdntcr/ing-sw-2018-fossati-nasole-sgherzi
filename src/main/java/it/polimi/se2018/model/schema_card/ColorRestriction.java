package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.GameColor;

public class ColorRestriction extends CellRestriction {

  private GameColor color;

  public ColorRestriction(GameColor color){
    this.color = color;
  }

  @Override
  public boolean isDiceAllowed() {
    return false;
  }

  public GameColor getColor() {
    return this.color;
  }
}
