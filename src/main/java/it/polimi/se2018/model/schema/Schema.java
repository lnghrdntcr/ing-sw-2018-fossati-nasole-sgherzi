package it.polimi.se2018.model.schema;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;

import java.awt.*;

/**
 * Contains the actual schema of a player and the relative associated schemaCardFace
 *
 * @author Nicola Fossati
 * @since 09/05/2018
 */
public class Schema {

    private DiceFace[][] diceFaces = new DiceFace[Settings.CARD_WIDTH][Settings.CARD_HEIGHT];
    private SchemaCardFace schemaCardFace;

    /**
     * Creates a new empty schema, associated with a schema
     *
     * @param schemaCardFace the schemaCard selected by the user to build this schema
     * @throws IllegalArgumentException if schemaCardFace is null
     */
    public Schema(@NotNull SchemaCardFace schemaCardFace) {
        if (schemaCardFace == null)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": schemaCardFace cannot be null!");
        this.schemaCardFace = schemaCardFace;
    }

    /**
     * Get a dice face placed in a specified point, or null if no dice was placed here
     *
     * @param point the position of the schema to check, 0 based
     * @return the diceFace placed in the given point or null
     */
    @Nullable
    public DiceFace getDiceFace(@NotNull Point point) {
        if (point == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": point cannot be null!");

        if (point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": illegal point: " + point.x + ", " + point.y + "!");

        return diceFaces[point.x][point.y];
    }

    /**
     * Set a diceFace in a specific point
     *
     * @param point    where to place a diceFace, 0 based
     * @param diceFace the diceFace to place or null to remove any existent diceFace
     * @throws IllegalArgumentException if point not valid, or point null
     */
    public void setDiceFace(@NotNull Point point, @Nullable DiceFace diceFace) {
        if (point == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": point cannot be null!");

        if (point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": illegal point: " + point.x + ", " + point.y + "!");


        diceFaces[point.x][point.y] = diceFace;
    }

    /**
     * Check if a given diceFace can be placed here, checking both the Schema and the SchemaCardFace
     *
     * @param point    the position where the dice should be placed, 0 based
     * @param diceFace the diceFace to place
     * @return true if the diceFace can be placed, false otherwise
     */
    public boolean isDiceAllowed(@NotNull Point point, @NotNull DiceFace diceFace) {
        if (point == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": point cannot be null!");

        if (diceFace == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null!");

        if (point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": illegal point: " + point.x + ", " + point.y + "!");

        if (diceFaces[point.x][point.y] != null) return false;

        if (!schemaCardFace.isDiceAllowed(point, diceFace)) return false;


        if (isEmpty()) {
            //If the schema is empty -> alias is the first dice
            return (point.x == 0 || point.y == 0 || point.x == Settings.CARD_WIDTH - 1 || point.y == Settings.CARD_HEIGHT - 1);
        } else {
            //There is at least one dice on the board
            //check if there is one at least neighbour
            if (!hasOneNeighbour(point)) {
                return false;
            }

            //check for similar neighbour
            if (hasOneSimilarNeighbour(point, diceFace)) {
                return false;
            }
        }


        return true;
    }


    /**
     * Checks if the selected cell has at least one neighbour
     *
     * @param point the position to check
     * @return true if has at least one neighbour, false otherwise
     */
    private boolean hasOneNeighbour(Point point) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!(x == 0 && y == 0)) {
                    int realX = point.x + x, realY = point.y + y;
                    if (realX > 0 && realY > 0 && realX < Settings.CARD_WIDTH && realY < Settings.CARD_HEIGHT) {
                        if (diceFaces[realX][realY] != null) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Checks if there is any neighbour (orthogonal) that is similar to a given DiceFace (same color || same number)
     *
     * @param point    the position to check
     * @param diceFace the dice to check
     * @return
     */
    private boolean hasOneSimilarNeighbour(Point point, DiceFace diceFace) {
        //UP
        if (point.y - 1 > 0) {
            if (diceFaces[point.x][point.y - 1] != null) {
                if (diceFaces[point.x][point.y - 1].isSimilar(diceFace)) {
                    return false;
                }
            }
        }
        //DOWN
        if (point.y + 1 < Settings.CARD_HEIGHT) {
            if (diceFaces[point.x][point.y + 1] != null) {
                if (diceFaces[point.x][point.y + 1].isSimilar(diceFace)) {
                    return false;
                }
            }
        }
        //LEFT
        if (point.x - 1 > 0) {
            if (diceFaces[point.x - 1][point.y] != null) {
                if (diceFaces[point.x - 1][point.y].isSimilar(diceFace)) {
                    return false;
                }
            }
        }
        //RIGHT
        if (point.x + 1 < Settings.CARD_WIDTH) {
            if (diceFaces[point.x + 1][point.y] != null) {
                if (diceFaces[point.x + 1][point.y].isSimilar(diceFace)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check if the schema is empty
     *
     * @return true if the schema is empty, false otherwise
     */
    public boolean isEmpty() {
        for (DiceFace[] column : diceFaces) {
            for (DiceFace cell : column) {
                if (cell != null) return false;
            }
        }
        return true;
    }

    public SchemaCardFace getSchemaCardFace() {
        return schemaCardFace;
    }
}
