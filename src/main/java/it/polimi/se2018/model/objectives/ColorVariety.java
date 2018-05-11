package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;
import java.util.Collections;
import java.util.EnumMap;

/**
 * Class that represents the Public Objective `Varietà di colore`
 * @author Francesco Sgherzi
 * @since 11/05/2018
 */
public class ColorVariety extends PublicObjective {

  // TODO: Write tests.

  public ColorVariety(int points) {
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

    EnumMap<GameColor, Integer> occurrences = new EnumMap<>(GameColor.class);
    Point actualPoint;
    DiceFace actualCell;

    if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

    // Initializing the number of occurrences for each color.
    for(GameColor gc: GameColor.values()){
      occurrences.put(gc, 0);
    }

    // Update the EnumMap with at each iteration, in order to get the number of occurrences of every color.
    for (int x = 0; x <= Settings.CARD_WIDTH; x++){
      for (int y = 0; y < Settings.CARD_HEIGHT; y++) {

        actualPoint = new Point(x, y);
        actualCell = schema.getDiceFace(actualPoint);

        if(actualCell != null){
          occurrences.put(actualCell.getColor(), occurrences.get(actualCell.getColor()) + 1);
        }

      }
    }

    // The minimum number of occurrences of a color will be the number of sets of different shades.
    return this.getPoint() * Collections.min(occurrences.values());

  }
}
