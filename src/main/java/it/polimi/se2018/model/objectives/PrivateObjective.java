package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import org.json.JSONObject;

import java.awt.*;
import java.io.Serializable;

import static it.polimi.se2018.utils.Settings.CARD_HEIGHT;
import static it.polimi.se2018.utils.Settings.CARD_WIDTH;

/**
 * Represents private objective cards.
 *
 * @since 09/05/2018
 */

public class PrivateObjective extends Objective implements Serializable {

    private GameColor color;

    public PrivateObjective(GameColor color) {
        this.color = color;
    }

    public static PrivateObjective fromJSON(JSONObject privateObjectiveJson) {
        String stringColor = privateObjectiveJson.getString("color");

        GameColor gc =null;
        for(GameColor tempColor : GameColor.values()){
            if(tempColor.toString().equalsIgnoreCase(stringColor)){
                gc=tempColor;
            }
        }

        if (gc == null) throw new IllegalArgumentException("Invalid game color received: "+stringColor);
        return new PrivateObjective(gc);
    }

    /**
     * @param schema The schema card we want to compute the score on.
     * @return The points scored with the private objective card.
     */
    @Override
    public int computeScore(Schema schema) {

        DiceFace actualDiceFace;
        Point actualPoint;

        if(schema == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

        int result = 0;
        for (int x = 0; x < CARD_WIDTH; x++) {
            for (int y = 0; y < CARD_HEIGHT; y++) {

                actualPoint = new Point(x, y);
                actualDiceFace = schema.getDiceFace(actualPoint);

                if(actualDiceFace != null){
                    if (actualDiceFace.getColor().equals(this.getColor())) {
                        result = result + schema.getDiceFace(actualPoint).getNumber();
                    }
                }

            }
        }
        return result;
    }

    public GameColor getColor() {
        return color;
    }
}
