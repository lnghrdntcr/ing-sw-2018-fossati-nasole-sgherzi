package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;

/**
 * Class that represents the Public Objective `Sfumature Medie`
 * @author Francesco Sgherzi
 * @since 11/05/2018
 */
public class MediumShades extends PublicObjective  {

  public MediumShades(int points) {
    super(points);
  }

  /**
   * Computes the score given a Schema, according to its logic.
   * @param schema The Schema on which compute the score.
   * @return The score given by the rule of this PublicObjective.
   * @throws IllegalArgumentException If the schema given is null.
   */
  @Override
  public int computeScore(Schema schema) {

    int numThree = 0;
    int numFour = 0;
    Point actualPoint;
    DiceFace actualFace;

    if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

    for(int x = 0; x < Settings.CARD_WIDTH; x++){
      for(int y = 0; y < Settings.CARD_HEIGHT; y++){

        actualPoint = new Point(x, y);
        actualFace = schema.getDiceFace(actualPoint);

        if(actualFace != null){
          if(actualFace.getNumber() == 3) numThree++;
          if(actualFace.getNumber() == 4) numFour++;
        }
      }
    }
    // The minimum number of occurrences of a 3 or 4 will be the number of sets.
    return numThree <= numFour ? this.getPoint() *  numThree : this.getPoint() * numFour;
  }
}
