package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;

public class SchemaCardFace {

  private Schema schema;
  private int difficulty;
  private CellRestriction[] cellRestrictions = new CellRestriction[Settings.CARD_HEIGHT * Settings.CARD_WIDTH];

  public static SchemaCardFace loadFromJson(){
    // TO ADD jsonObject as a parameter
    return null;
  }

  private SchemaCardFace (){

  }

  public CellRestriction getCellRestriction(Point point){
    return null;
  }

  private void setCellRestriction(Point point){
    return;
  }

  public boolean isDiceAllowed(Point point){
    return false;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(int difficulty) {
    this.difficulty = difficulty;
  }
}
