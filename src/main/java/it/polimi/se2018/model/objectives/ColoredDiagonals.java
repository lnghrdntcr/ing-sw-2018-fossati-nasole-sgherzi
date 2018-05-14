package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        boolean[][] visited = new boolean[Settings.CARD_WIDTH][CARD_HEIGHT];

        if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                int scoreTemp = this.countDiag(visited, schema, null, new Point(x, y));
                if(scoreTemp > 1) score += scoreTemp;
            }
        }

        return score;
    }

    private int countDiag(boolean[][] visited, Schema schema, GameColor color, Point point){

        if(point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT) return 0;
        if(schema.getDiceFace(point) == null) return 0;
        if(color == null) color = schema.getDiceFace(point).getColor();
        if(visited[point.x][point.y]) return 0;
        if(schema.getDiceFace(point).getColor() != color ) return 0;

        visited[point.x][point.y] = true;

        return 1
                + countDiag(visited, schema, color, new Point(point.x + 1, point.y + 1))
                + countDiag(visited, schema, color, new Point(point.x - 1, point.y + 1))
                + countDiag(visited, schema, color, new Point(point.x + 1, point.y - 1))
                + countDiag(visited, schema, color, new Point(point.x - 1, point.y - 1));

    }

}
