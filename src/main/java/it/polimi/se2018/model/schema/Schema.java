package it.polimi.se2018.model.schema;

import it.polimi.se2018.model.ImmutableCloneable;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;

import java.awt.*;

/**
 * Contains the actual schema of a player and the relative associated schemaCardFace
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
    public Schema(SchemaCardFace schemaCardFace) {
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
    public DiceFace getDiceFace(Point point) {
        if (point == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": point cannot be null!");

        if (point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": illegal point: " + point.x + ", " + point.y + "!");

        return diceFaces[point.x][point.y];
    }

    /**
     * Set a diceFace in a specific point. The Schema must be empty in the point.
     *
     * @param point    where to place a diceFace, 0 based
     * @param diceFace the diceFace to place
     * @throws IllegalArgumentException if point not valid, or point null
     * @throws IllegalStateException    if the cell is already occupied
     */
    public void setDiceFace(Point point, DiceFace diceFace) {
        if (point == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": point cannot be null!");

        if (diceFace == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null!");

        if (point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": illegal point: " + point.x + ", " + point.y + "!");

        if (diceFaces[point.x][point.y] != null)
            throw new IllegalStateException(getClass().getCanonicalName() + ": cannot put a dice in an already occupied face!");


        diceFaces[point.x][point.y] = diceFace;
    }

    /**
     * Remove and returns a DiceFace in the Schema
     * @param point where to remove the DiceFace
     * @return the removed DiceFace
     */
    public DiceFace removeDiceFace(Point point){
        if (diceFaces[point.x][point.y] == null)
            throw new IllegalStateException(getClass().getCanonicalName() + ": cannot remove dice from an empty cell!");

        DiceFace df = diceFaces[point.x][point.y];
        diceFaces[point.x][point.y] = null;
        return df;
    }

    /**
     * Check if a given diceFace can be placed here, checking both the Schema and the SchemaCardFace
     *
     * @param point    the position where the dice should be placed, 0 based
     * @param diceFace the diceFace to place
     * @param ignore the type of restricton to ignore
     * @return true if the diceFace can be placed, false otherwise
     */
    public boolean isDiceAllowed(Point point, DiceFace diceFace, SchemaCardFace.Ignore ignore) {
        return isDiceAllowed(point, diceFace, ignore, false);
    }

    /**
     * Check if a given diceFace can be placed here, checking both the Schema and the SchemaCardFace
     *
     * @param point    the position where the dice should be placed, 0 based
     * @param diceFace the diceFace to place
     * @param ignore the type of restricton to ignore
     * @param forceLoneliness if this diceFace should be kept alone (as for toolcard 9)
     * @return true if the diceFace can be placed, false otherwise
     */
    public boolean isDiceAllowed(Point point, DiceFace diceFace, SchemaCardFace.Ignore ignore, boolean forceLoneliness) {
        if (point == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": point cannot be null!");

        if (diceFace == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null!");

        if (point.x < 0 || point.x >= Settings.CARD_WIDTH || point.y < 0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": illegal point: " + point.x + ", " + point.y + "!");

        if (diceFaces[point.x][point.y] != null) return false;

        if (!schemaCardFace.isDiceAllowed(point, diceFace, ignore)) return false;

        if(forceLoneliness){
            return !hasOneNeighbour(point);
        }else {

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
                    if (realX >= 0 && realY >= 0 && realX < Settings.CARD_WIDTH && realY < Settings.CARD_HEIGHT) {
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
     * @return true if it has a similar neighbour, false otherwise.
     */
    private boolean hasOneSimilarNeighbour(Point point, DiceFace diceFace) {
        //UP
        if (point.y - 1 >= 0) {
            if (diceFaces[point.x][point.y - 1] != null) {
                if (diceFaces[point.x][point.y - 1].isSimilar(diceFace)) {
                    return true;
                }
            }
        }
        //DOWN
        if (point.y + 1 < Settings.CARD_HEIGHT) {
            if (diceFaces[point.x][point.y + 1] != null) {
                if (diceFaces[point.x][point.y + 1].isSimilar(diceFace)) {
                    return true;
                }
            }
        }
        //LEFT
        if (point.x - 1 >= 0) {
            if (diceFaces[point.x - 1][point.y] != null) {
                if (diceFaces[point.x - 1][point.y].isSimilar(diceFace)) {
                    return true;
                }
            }
        }
        //RIGHT
        if (point.x + 1 < Settings.CARD_WIDTH) {
            if (diceFaces[point.x + 1][point.y] != null) {
                if (diceFaces[point.x + 1][point.y].isSimilar(diceFace)) {
                    return true;
                }
            }
        }

        return false;
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

    public int computeFreeSpaces(){

        int freeSpaces = 0;

        for (int x = 0; x < Settings.CARD_HEIGHT; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                if(this.diceFaces[x][y] == null) freeSpaces++;
            }

        }

        return freeSpaces;
    }

    public SchemaCardFace getSchemaCardFace() {
        return schemaCardFace;
    }

    public Schema clone() {
        Schema newSchema = new Schema(schemaCardFace);
        newSchema.diceFaces=this.diceFaces.clone();
        return newSchema;
    }

    public boolean isDiceAllowedSomewhere(DiceFace diceFace, SchemaCardFace.Ignore ignore){
        for (int x = 0; x < Settings.CARD_HEIGHT; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                if(isDiceAllowed(new Point(x, y), diceFace, ignore)){
                    return true;
                }
            }

        }

        return false;
    }
}
