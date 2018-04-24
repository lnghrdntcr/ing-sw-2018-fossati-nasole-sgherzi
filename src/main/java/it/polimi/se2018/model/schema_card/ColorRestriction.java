package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.GameColor;

public class ColorRestriction extends CellRestriction {

  private GameColor color;

  ColorRestriction(GameColor color){

  }

  @Override
  public boolean isDiceAllowed() {
    return false;
  }
}
