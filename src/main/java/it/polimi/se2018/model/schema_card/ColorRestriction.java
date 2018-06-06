package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;

/**
 * @since 09/05/2018
 * A restriction based on the color of the dice
 */
public class ColorRestriction extends CellRestriction {

    private GameColor color;

    /**
     * @param color the color of this restriction
     */
    public ColorRestriction(GameColor color) {
        if(color==null){
            throw new IllegalArgumentException(this.getClass().getCanonicalName()+": color must not be null!");
        }
        this.color = color;
    }

    public GameColor getColor() {
        return this.color;
    }

    /**
     * @param diceFace the DiceFace to check
     * @return true if the diceFace's color is equal to this restriction's color, false otherwise
     */
    @Override
    public boolean isDiceAllowed(DiceFace diceFace) {
        return diceFace.getColor().equals(color);
    }

    @Override
    public String toString() {
        return color.toString();
    }
}
