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

public class MediumShadesTest {


  List<SchemaCard> loadedSchemas;
  MediumShades mediumShades;
  Schema actualSchemaCardFront;
  Schema actualSchemaCardBack;


  @Before
  public void setUp() throws Exception {


    this.mediumShades= new MediumShades();

    // Take all the cards...
    try {
      this.loadedSchemas = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_EqualCards.scf");
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }

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
      actualSchemaCardFront.setDiceFace(new Point(4, 1), new DiceFace(GameColor.BLUE, 2));

      // 2
      actualSchemaCardFront.setDiceFace(new Point(0, 2), new DiceFace(GameColor.PURPLE, 2));
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
      actualSchemaCardBack.setDiceFace(new Point(0, 2), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardBack.setDiceFace(new Point(1, 2), new DiceFace(GameColor.BLUE, 3));
      actualSchemaCardBack.setDiceFace(new Point(2, 2), new DiceFace(GameColor.GREEN, 1));
      actualSchemaCardBack.setDiceFace(new Point(3, 2), new DiceFace(GameColor.BLUE, 4));
      actualSchemaCardBack.setDiceFace(new Point(4, 2), new DiceFace(GameColor.GREEN, 1));

      // 3
      actualSchemaCardBack.setDiceFace(new Point(0, 3), new DiceFace(GameColor.BLUE, 5));
      actualSchemaCardBack.setDiceFace(new Point(1, 3), new DiceFace(GameColor.GREEN, 2));
      actualSchemaCardBack.setDiceFace(new Point(2, 3), new DiceFace(GameColor.BLUE, 1));
      actualSchemaCardBack.setDiceFace(new Point(3, 3), new DiceFace(GameColor.PURPLE, 2));
      actualSchemaCardBack.setDiceFace(new Point(4, 3), new DiceFace(GameColor.BLUE, 1));

    }



  }

  @Test
  public void computeScore() {

    // Test against a null input.
    try{
      this.mediumShades.computeScore(null);
      fail();
    } catch (IllegalArgumentException e){}

    assertEquals(2, this.mediumShades.computeScore(this.actualSchemaCardBack));
    assertEquals(0, this.mediumShades.computeScore(this.actualSchemaCardFront));
  }
}