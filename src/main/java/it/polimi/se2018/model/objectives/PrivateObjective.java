package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;

import java.awt.*;

import static it.polimi.se2018.utils.Settings.CARD_HEIGHT;
import static it.polimi.se2018.utils.Settings.CARD_WIDTH;

/**
 * Represents private objective cards.
 *
 * @author Angelo Nasole
 * @since 09/05/2018
 */

public class PrivateObjective extends Objective {

    private GameColor color;

    public PrivateObjective(GameColor color) {
        this.color = color;
    }

    /**
     * @param schema The schema card we want to compute the score on.
     * @return The points scored with the private objective card.
     */
    @Override
    public int computeScore(Schema schema) {
        int result = 0;
        for (int i = 0; i < CARD_HEIGHT; i++) {
            for (int j = 0; j < CARD_WIDTH; j++) {
                if (schema.getDiceFace(new Point(i, j)).getColor().equals(this.getColor())) {
                    result = result + schema.getDiceFace(new Point(i, j)).getNumber();
                }
            }
        }
        return result;
    }

    public GameColor getColor() {
        return color;
    }
}
