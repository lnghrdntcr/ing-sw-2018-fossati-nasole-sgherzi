package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.Schema;

import java.awt.*;

import static it.polimi.se2018.utils.Settings.CARD_HEIGHT;
import static it.polimi.se2018.utils.Settings.CARD_WIDTH;

/**
 * Represents public objective card that checks that in the columns are located no duplicate dice numbers.
 *
 * @author Angelo Nasole
 * @since 10/05/2018
 */
public class DifferentColumnShades extends PublicObjective {

    public DifferentColumnShades() {
        super(4);
    }

    /**
     * @param schema The schema card we want to compute the score on.
     * @return The points scored with the public objective card.
     */
    @Override
    public int computeScore(Schema schema) {
        int score = 0;


        for (int x = 0; x < CARD_WIDTH; x++) {
            //creating an array to count the occurrences of every dice face number, initialized to 0
            int[] counter = new int[6];
            for (int i = 0; i < 6; i++) {
                counter[i] = 0;
            }

            for (int y = 0; y < CARD_HEIGHT; y++) {

                if (schema.getDiceFace(new Point(x, y)) == null) {
                    //do nothing ¯\_(ツ)_/¯
                } else {
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

        if (filled < CARD_HEIGHT) return false;

        return true;
    }

}
