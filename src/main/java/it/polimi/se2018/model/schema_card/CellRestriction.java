package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;

import java.io.Serializable;

/**
 * @since 09/05/2018
 * This class represents a general restriction used in SchemaCard
 */
public abstract class CellRestriction implements Serializable {
    /**
     * @param diceFace the DiceFace to check
     * @return true if the diceface can be placed over this restriction, false otherwise
     */
    public abstract boolean isDiceAllowed(DiceFace diceFace);

    /**
     * Get the correct restriction from a string. If it's a valid number, the restriction will be a NumberRestriction;
     * if it's a blank string, the restriction will be a NoRestriction; if it's a valid color, the restriction will be
     * a ClorRestriction
     *
     * @param restr the restriction to parse
     * @return a CellRestriction based on the input
     * @throws IllegalArgumentException if no valid restriction can be found for the input
     */
    public static CellRestriction getRestrictionFromString(String restr) {
        if (restr == null)
            throw new IllegalArgumentException("restr must not be null!");

        if (restr.equals("")) {
            return new NoRestriction();
        }

        Integer integer = null;
        try {
            integer = Integer.parseInt(restr);
        } catch (Exception ignored) {
        }

        if (integer != null) {
            return new NumberRestriction(integer);
        }

        for (GameColor gc : GameColor.values()) {
            if (restr.equals(gc.toString())) {
                return new ColorRestriction(gc);
            }
        }

        throw new IllegalArgumentException("No restriction found for '" + restr + "'");

    }
}
