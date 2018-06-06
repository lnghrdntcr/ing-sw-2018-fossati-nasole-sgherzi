package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;

/**
 * @since 09/05/2018
 * A restriction based on the number of the dice
 */
public class NumberRestriction extends CellRestriction {

    private int number;

    /**
     * @param number the value of the dice required
     */
    public NumberRestriction(int number) {
        if (number < 1 || number > 6) {
            throw new IllegalArgumentException(this.getClass().getCanonicalName()+": number must be between 1 and 6!");
        }
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    /**
     * @param diceFace the DiceFace to check
     * @return true if the dice number is equal to the restriction number, false otherwise
     */
    @Override
    public boolean isDiceAllowed(DiceFace diceFace) {
        return (number == diceFace.getNumber());
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
