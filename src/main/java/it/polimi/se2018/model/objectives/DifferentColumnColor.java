package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
        import it.polimi.se2018.model.schema.Schema;

        import java.awt.*;
        import java.util.EnumMap;

        import static it.polimi.se2018.utils.Settings.CARD_HEIGHT;
        import static it.polimi.se2018.utils.Settings.CARD_WIDTH;

/**
 * Represents public objective card that checks that in the column are located dice of as many colours as possible.
 *
 * @author Angelo Nasole
 * @since 09/05/2018
 */

public class DifferentColumnColor extends PublicObjective {

    public DifferentColumnColor(int points) {
        super(5);
    }

    /**
     * @param schema The schema card we want to compute the score on.
     * @return The points scored with the public objective card.
     */
    @Override
    public int computeScore(Schema schema) {

        int score = 0;
        DiceFace actualDiceFace;
        GameColor actualColor;

        if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

        for (int x = 0; x < CARD_WIDTH; x++) {

            //initializes Map color->#of dice
            EnumMap<GameColor, Integer> counter = new EnumMap<>(GameColor.class);
            for (GameColor gc : GameColor.values()) {
                counter.put(gc, 0);
            }

            for (int y = 0; y < CARD_HEIGHT; y++) {

                actualDiceFace = schema.getDiceFace(new Point(x, y));

                if(actualDiceFace != null){
                    actualColor = actualDiceFace.getColor();
                    counter.put(actualColor, counter.get(actualColor) + 1);
                }

            }

            if (isValid(counter)) score = score + getPoint();

        }
        return score;
    }

    /**
     * @param counter A map using colors as key and their occurrences in the column as value.
     * @return true if all the colors are different, false otherwise.
     */
    private boolean isValid(EnumMap<GameColor, Integer> counter) {
        int zeros = 0;
        int filled = 0;

        for (GameColor gc : GameColor.values()) {
            if (counter.get(gc) == 0) zeros++;
            filled = filled + counter.get(gc);
        }

        if (filled < CARD_HEIGHT) return false;

        return zeros < GameColor.values().length - CARD_HEIGHT + 1;
    }
}