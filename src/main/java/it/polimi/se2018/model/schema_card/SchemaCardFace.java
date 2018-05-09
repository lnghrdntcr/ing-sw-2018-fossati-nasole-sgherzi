package it.polimi.se2018.model.schema_card;

import com.sun.istack.internal.NotNull;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Settings;
import javafx.scene.control.Cell;

import java.awt.*;

/**
 * @author Nicola Fossati
 * @since 09/05/2018
 * Class that represents a Schema, with its restrictions
 */
public class SchemaCardFace {

    private int difficulty;
    private CellRestriction[][] cellRestrictions = new CellRestriction[Settings.CARD_WIDTH][Settings.CARD_HEIGHT];

    public static SchemaCardFace loadFromJson() {
        //TODO: implement this method!
        return null;
    }

    /**
     * @param difficulty the difficulty of the Schema, must be greater than 0
     */
    private SchemaCardFace(int difficulty) {
        if ( difficulty <=0) throw new  IllegalArgumentException(this.getClass().getCanonicalName()+": difficulty must be greater than 0!");
        this.difficulty=difficulty;
    }

    /**
     * Set the restriction on a specific position
     * @param point the position of the restriction, 0 based
     * @param restriction the restriction to apply
     * @throws IllegalArgumentException if the position is invalid, or the restriction or point is null
     */
    private void setCellRestriction(@NotNull Point point, @NotNull CellRestriction restriction) {
        if (restriction == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": restriction cannot be null!");

        if(point == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": point cannot be null!");

        if(point.x <0 || point.x >= Settings.CARD_WIDTH || point.y <0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName()+": illegal point: "+point.x+", "+point.y+"!");

        cellRestrictions[point.x][point.y]=restriction;

    }

    /**
     * Checks if a dice is alowed in a specific position
     * @param point the position to check
     * @param diceFace the dice to check
     * @return true if the dice can be placed in point, false otherwise
     * @throws IllegalArgumentException if the position is invalid, or the restriction or point is null
     */
    public boolean isDiceAllowed(@NotNull Point point, @NotNull DiceFace diceFace) {
        if (diceFace == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": diceFace cannot be null!");

        return getRestriction(point).isDiceAllowed(diceFace);
    }

    /**
     * Get the restriction of a specific position
     * @param point the point of the schema, 0 based
     * @return the restriction in that cell
     */
    @NotNull public CellRestriction getRestriction(@NotNull Point point){
        if(point == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": point cannot be null!");

        if(point.x <0 || point.x >= Settings.CARD_WIDTH || point.y <0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName()+": illegal point: "+point.x+", "+point.y+"!");

        assert cellRestrictions[point.x][point.y]!= null: "cellRestriction is null!";

        return cellRestrictions[point.x][point.y];
    }

    /**
     * @return the difficulty of this SchemaCard
     */
    public int getDifficulty() {
        return difficulty;
    }

}














