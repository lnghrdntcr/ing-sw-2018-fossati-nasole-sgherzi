package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.Schema;

import java.awt.*;

import static it.polimi.se2018.utils.Settings.CARD_HEIGHT;
import static it.polimi.se2018.utils.Settings.CARD_WIDTH;


/**
 * Represents public objective card that checks that in the rows are located no duplicate dice numbers.
 *
 * @author Angelo Nasole
 * @since 10/05/2018
 */

public class DifferentRowShades extends PublicObjective {

    public DifferentRowShades() {
        super(5);
    }

    /**
     * @param schema The schema card we want to compute the score on.
     * @return The points scored with the public objective card.
     */
    @Override
    public int computeScore(Schema schema) {
        int score = 0;

        if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

        for (int y = 0; y < CARD_HEIGHT; y++) {
            //creating an array to count the occurrences of every dice face number, initialized to 0
            int[] counter = new int[6];
            for (int i = 0; i < 6; i++) {
                counter[i] = 0;
            }

            for (int x = 0; x < CARD_WIDTH; x++) {

                if (schema.getDiceFace(new Point(x, y)) != null) {
                    //¯\_(ツ)_/¯
                    counter[schema.getDiceFace(new Point(x, y)).getNumber() - 1]++;
                }
            }

            if (isValid(counter)) score = score + getPoint();

        }


        return score;
    }

    /**
     * @param counter An array counting the occurrences of every dice face number.
     * @return true if all the faces are different, false otherwise.
     */
    private boolean isValid(int[] counter) {

        int filled = 0;

        for (int i = 0; i < counter.length; i++) {
            if (counter[i] > 1) return false;
            else filled = filled + counter[i];
        }

        if (filled < CARD_WIDTH) return false;

        return true;
    }


}
