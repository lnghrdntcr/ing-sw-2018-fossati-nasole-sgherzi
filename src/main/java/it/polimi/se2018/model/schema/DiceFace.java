package it.polimi.se2018.model.schema;

import com.sun.istack.internal.NotNull;

/**
 * Holds the logic for the dice to be placed in the schema card or the scoreboard
 * @author Francesco Sgherzi
 * @since 09/05/2018
 */
public class DiceFace {
    // TODO: Implement Tests
    private GameColor color;
    private int number;

    /**
     * @param color  The color of the Dice Face.
     * @param number The number of the Dice Face.
     * @throws IllegalArgumentException if the number is less than 1 or greater than 6.
     */
    public DiceFace(@NotNull GameColor color, int number) {
        if (this.number < 1 || this.number > 6)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Number must be greater than one or less than 6.");
        if (color == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Color cannot be null.");
        this.color = color;
        this.number = number;
    }

    public GameColor getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Check if a dice is similar to this dice
     * @param diceFace the other DiceFace to compare to this one
     * @return true if color or (inclusively) number is equal, false otherwise
     * @author Nicola Fossati
     */
    public boolean isSimilar(DiceFace diceFace) {
        return (this.color.equals(diceFace.getColor()) || this.number == diceFace.getNumber());
    }
}
