package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2018.utils.Settings.CARD_HEIGHT;
import static it.polimi.se2018.utils.Settings.CARD_WIDTH;
/**
 * Represents public objective card that checks that computes score based on the repeated colors on diagonals.
 *
 * @author Angelo Nasole
 * @since 10/05/2018
 */
public class ColoredDiagonals extends PublicObjective {

    public ColoredDiagonals(int points) {
        super(1);
    }

    /**
     * @param schema The schema card we want to compute the score on.
     * @return The points scored with the public objective card.
     */
    @Override
    public int computeScore(Schema schema) {
        int score = 0;
        //From right to left
        for (int x = CARD_WIDTH - 1; x >= 0; x--) {
            int row = 1;
            ArrayList<GameColor> diag = new ArrayList<>();
            for (int y = 0; y < row; y++) {
                int i = x;
                diag.add(schema.getDiceFace(new Point(i++, y)).getColor());
            }
            if (row < CARD_HEIGHT - 1) row++;

            for (int a = 0; a < diag.size(); a++) {
                int count = 0;
                if (diag.get(a).equals(diag.get(a + 1))) count = count + getPoint();
                if (count > 1) score = score + count;
            }

        }

        //From left to right
        for (int x = 0; x < CARD_WIDTH; x++) {
            int row = 1;
            ArrayList<GameColor> diag = new ArrayList<>();
            for (int y = 0; y < row; y++) {
                int i = x;
                diag.add(schema.getDiceFace(new Point(i--, y)).getColor());
            }
            if (row < CARD_HEIGHT - 1) row++;

            for (int a = 0; a < diag.size(); a++) {
                int count = 0;
                if (diag.get(a).equals(diag.get(a + 1))) count = count + getPoint();
                if (count > 1) score = score + count;
            }

        }

        return score;
    }
}
