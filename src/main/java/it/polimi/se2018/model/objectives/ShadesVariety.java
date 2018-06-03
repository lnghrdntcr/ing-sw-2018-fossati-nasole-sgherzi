package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;

/**
 * Class that represents the Public Objective `Sfumature diverse`
 * @since 11/05/2018
 */
public class ShadesVariety extends PublicObjective  {

  public ShadesVariety() {
    super(5);
  }

  /**
   * Computes the score given a Schema, according to its logic.
   * @param schema The Schema on which compute the score.
   * @return The score given by the rule of this PublicObjective.
   * @throws IllegalArgumentException If the given schema is null.
   */
  @Override
  public int computeScore(Schema schema) {

    HashMap<Integer, Integer> occurrences = new HashMap<>();
    Point actualPoint;
    DiceFace actualCell;

    if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

    // Initializing the number of occurrences for each value.
    for (int i = 1; i <= 6; i++) {
      occurrences.put(i, 0);
    }

    for(int x = 0; x < Settings.CARD_WIDTH; x++){
      for (int y = 0; y < Settings.CARD_HEIGHT; y++) {

        actualPoint = new Point(x, y);
        actualCell = schema.getDiceFace(actualPoint);

        if(actualCell != null){
          occurrences.put(actualCell.getNumber(), occurrences.get(actualCell.getNumber()) + 1);
        }

      }
    }

    // The minimum number of occurrences of a value will be the number of sets of different shades.
    return this.getPoint() * Collections.min(occurrences.values());

  }
}
