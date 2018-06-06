package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;

/**
 * @since 09/05/2018
 * A fake restriction (a dice is always allowed over this restriction)
 */
public class NoRestriction extends CellRestriction {

    /**
     * @param diceFace the DiceFace to check
     * @return always return true (every dice allowed over this restriction)
     */
    @Override
    public boolean isDiceAllowed(DiceFace diceFace) {
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
}
