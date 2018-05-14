package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.Side;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.*;

public class ColoredDiagonalsTest {


  List<SchemaCard> loadedSchemas;
  ColoredDiagonals coloredDiagonals;
  Schema actualSchemaCardFront;
  Schema actualSchemaCardBack;

  @Before
  public void setUp() throws FileNotFoundException {

    this.coloredDiagonals = new ColoredDiagonals(2);

    // Take all the cards...
    this.loadedSchemas = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_EqualCards.scf");

    // and generate a Schema from it.
    for(SchemaCard sc: this.loadedSchemas){

      actualSchemaCardFront = new Schema(sc.getFace(Side.FRONT));
      actualSchemaCardBack = new Schema(sc.getFace(Side.BACK));

      // 0
      actualSchemaCardFront.setDiceFace(new Point(0, 0), new DiceFace(GameColor.PURPLE, 4));
      actualSchemaCardFront.setDiceFace(new Point(2, 0), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardFront.setDiceFace(new Point(3, 0), new DiceFace(GameColor.BLUE, 5));

      // 1
      actualSchemaCardFront.setDiceFace(new Point(1, 1), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardFront.setDiceFace(new Point(2, 1), new DiceFace(GameColor.BLUE, 6));
      actualSchemaCardFront.setDiceFace(new Point(3, 1), new DiceFace(GameColor.PURPLE, 1));
      actualSchemaCardFront.setDiceFace(new Point(4, 1), new DiceFace(GameColor.BLUE, 2));

      // 2
      actualSchemaCardFront.setDiceFace(new Point(0, 2), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardFront.setDiceFace(new Point(1, 2), new DiceFace(GameColor.BLUE, 3));
      actualSchemaCardFront.setDiceFace(new Point(3, 2), new DiceFace(GameColor.BLUE, 4));
      actualSchemaCardFront.setDiceFace(new Point(4, 2), new DiceFace(GameColor.GREEN, 1));

      // 3
      actualSchemaCardFront.setDiceFace(new Point(0, 3), new DiceFace(GameColor.BLUE, 5));
      actualSchemaCardFront.setDiceFace(new Point(1, 3), new DiceFace(GameColor.GREEN, 2));
      actualSchemaCardFront.setDiceFace(new Point(2, 3), new DiceFace(GameColor.BLUE, 1));
      actualSchemaCardFront.setDiceFace(new Point(3, 3), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardFront.setDiceFace(new Point(4, 3), new DiceFace(GameColor.BLUE, 1));

      // 0
      actualSchemaCardBack.setDiceFace(new Point(0, 0), new DiceFace(GameColor.PURPLE, 4));
      actualSchemaCardBack.setDiceFace(new Point(1, 0), new DiceFace(GameColor.BLUE, 1));
      actualSchemaCardBack.setDiceFace(new Point(2, 0), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardBack.setDiceFace(new Point(3, 0), new DiceFace(GameColor.BLUE, 5));
      actualSchemaCardBack.setDiceFace(new Point(4, 0), new DiceFace(GameColor.GREEN, 1));

      // 1
      actualSchemaCardBack.setDiceFace(new Point(0, 1), new DiceFace(GameColor.BLUE, 1));
      actualSchemaCardBack.setDiceFace(new Point(1, 1), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardBack.setDiceFace(new Point(2, 1), new DiceFace(GameColor.BLUE, 6));
      actualSchemaCardBack.setDiceFace(new Point(3, 1), new DiceFace(GameColor.GREEN, 1));
      actualSchemaCardBack.setDiceFace(new Point(4, 1), new DiceFace(GameColor.BLUE, 2));

      // 2
      actualSchemaCardBack.setDiceFace(new Point(0, 2), new DiceFace(GameColor.GREEN, 2));
      actualSchemaCardBack.setDiceFace(new Point(1, 2), new DiceFace(GameColor.BLUE, 3));
      actualSchemaCardBack.setDiceFace(new Point(2, 2), new DiceFace(GameColor.GREEN, 1));
      actualSchemaCardBack.setDiceFace(new Point(3, 2), new DiceFace(GameColor.BLUE, 4));
      actualSchemaCardBack.setDiceFace(new Point(4, 2), new DiceFace(GameColor.GREEN, 1));

      // 3
      actualSchemaCardBack.setDiceFace(new Point(0, 3), new DiceFace(GameColor.RED, 5));
      actualSchemaCardBack.setDiceFace(new Point(1, 3), new DiceFace(GameColor.GREEN, 2));
      actualSchemaCardBack.setDiceFace(new Point(2, 3), new DiceFace(GameColor.BLUE, 1));
      actualSchemaCardBack.setDiceFace(new Point(3, 3), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardBack.setDiceFace(new Point(4, 3), new DiceFace(GameColor.BLUE, 1));

    }

  }


  @Test
  public void computeScore() {

    try{
      this.coloredDiagonals.computeScore(null);
      fail();
    } catch (IllegalArgumentException e){}

    assertEquals(18, this.coloredDiagonals.computeScore(this.actualSchemaCardBack));
    assertEquals(13, this.coloredDiagonals.computeScore(this.actualSchemaCardFront));

  }
}