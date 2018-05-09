package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;

/**
 * @author Nicola Fossati
 * @since 09/05/2018
 * This class represents a general restriction used in SchemaCard
 */
public abstract class CellRestriction {
    /**
     * @param diceFace the DiceFace to check
     * @return true if the diceface can be placed over this restriction, false otherwise
     */
    public abstract boolean isDiceAllowed(DiceFace diceFace);
}
