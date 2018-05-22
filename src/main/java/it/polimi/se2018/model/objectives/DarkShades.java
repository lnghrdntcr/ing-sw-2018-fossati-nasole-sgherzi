package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;

/**
 * Class that represents the Public Objective `Sfumature Scure`.
 * @author Francesco Sgherzi
 * @since 11/05/2018
 */
public class DarkShades extends PublicObjective {

  public DarkShades() {
    super(2);
  }

  /**
   * Computes the score given a Schema, according to its logic.
   * @param schema The Schema on which compute the score.
   * @return The score given by the rule of this PublicObjective.
   * @throws IllegalArgumentException If the schema given is null.
   */
  @Override
  public int computeScore(Schema schema) {

    int numFive = 0;
    int numSix = 0;
    Point actualPoint;
    DiceFace actualFace;

    if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

    for(int x = 0; x < Settings.CARD_WIDTH; x++){
      for(int y = 0; y < Settings.CARD_HEIGHT; y++){

        actualPoint = new Point(x, y);
        actualFace = schema.getDiceFace(actualPoint);

        if(actualFace != null){
          if(schema.getDiceFace(actualPoint).getNumber() == 5) numFive++;
          if(schema.getDiceFace(actualPoint).getNumber() == 6) numSix++;
        }

      }
    }
    // The minimum number of occurrences of a 5 or a six will be the number of sets.
    return numFive <= numSix ? this.getPoint() * numFive : this.getPoint() * numSix;
  }
}
