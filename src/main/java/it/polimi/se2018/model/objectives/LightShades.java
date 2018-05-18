package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;

/**
 * Class that represents the Public Objective `Sfumature Chiare`.
 * @author Francesco Sgherzi
 * @since 11/05/2018
 */
public class LightShades extends PublicObjective  {

  public LightShades(int points) {
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

    int numOne = 0;
    int numTwo = 0;
    Point actualPoint;
    DiceFace actualFace;

    if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

    for(int x = 0; x < Settings.CARD_WIDTH; x++){
      for(int y = 0; y < Settings.CARD_HEIGHT; y++){

        actualPoint = new Point(x, y);
        actualFace = schema.getDiceFace(actualPoint);

        if(actualFace != null){
          if(actualFace.getNumber() == 1) numOne++;
          if(actualFace.getNumber() == 2) numTwo++;
        }

      }
    }
    // The minimum number of occurrences of a 1 or 2 will be the number of sets.
    return numOne <= numTwo ? (this.getPoint() * numOne) : (this.getPoint() * numTwo);
  }
}
