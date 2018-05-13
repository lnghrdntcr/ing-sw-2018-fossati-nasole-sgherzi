package it.polimi.se2018.model.schema_card;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Settings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;

/**
 * @author Nicola Fossati
 * @since 09/05/2018
 * Class that represents a Schema, with its restrictions
 */
public class SchemaCardFace {

    private int difficulty;
    private String name;
    private CellRestriction[][] cellRestrictions = new CellRestriction[Settings.CARD_WIDTH][Settings.CARD_HEIGHT];

    /**
     * Load a SchemaCardFace as declared from JSON
     * @param obj a JSONObject that contains the specifications
     * @return a SchemaCardFaces as declared in the JSONObject
     * @throws JSONException if the JSON is malformed
     * @throws MalformedSchemaCardFaceException if the restrictions dimension are not valid
     */
    public static SchemaCardFace loadFromJson(JSONObject obj) throws JSONException, MalformedSchemaCardFaceException {
        int difficulty = obj.getInt("diff");
        String name = obj.getString("name");
        JSONArray rows = obj.getJSONArray("restrictions");

        if(rows.length() != Settings.CARD_HEIGHT){
            throw new MalformedSchemaCardFaceException("The face should be "+Settings.CARD_HEIGHT+" cells height, but this face is "+rows.length()+" cells height!");
        }

        SchemaCardFace face = new SchemaCardFace(difficulty, name);

        for(int y=0; y<Settings.CARD_HEIGHT; y++){
            JSONArray row = rows.getJSONArray(y);

            if(row.length()!=Settings.CARD_WIDTH){
                throw new MalformedSchemaCardFaceException("The face should be "+Settings.CARD_WIDTH+" cells width, but this face is "+row.length()+" cells width!");
            }

            for(int x=0; x<Settings.CARD_WIDTH; x++){
                face.setCellRestriction(new Point(x, y), CellRestriction.getRestrictionFromString(row.getString(x)));
            }

        }


        return face;
    }

    /**
     * @param difficulty the difficulty of the Schema, must be greater than 0
     * @param name a name for the SchemaCardFace (this will be shown to the user)
     */
    private SchemaCardFace(int difficulty, String name) {
        if ( difficulty <=0) throw new  IllegalArgumentException(this.getClass().getCanonicalName()+": difficulty must be greater than 0!");
        if (name == null) throw new  IllegalArgumentException(getClass().getCanonicalName()+": name cannot be null!");
        this.difficulty=difficulty;
        this.name = name;
    }

    /**
     * Set the restriction on a specific position
     * @param point the position of the restriction, 0 based
     * @param restriction the restriction to apply
     * @throws IllegalArgumentException if the position is invalid, or the restriction or point is null
     */
    private void setCellRestriction(Point point, CellRestriction restriction) {
        if (restriction == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": restriction cannot be null!");

        if(point == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": point cannot be null!");

        //Is this even necessary? It's an internal call and it's assured what checked...
        /*if(point.x <0 || point.x >= Settings.CARD_WIDTH || point.y <0 || point.y >= Settings.CARD_HEIGHT)
            throw new IllegalArgumentException(this.getClass().getCanonicalName()+": illegal point: "+point.x+", "+point.y+"!");*/

        cellRestrictions[point.x][point.y]=restriction;

    }

    /**
     * Checks if a dice is alowed in a specific position
     * @param point the position to check
     * @param diceFace the dice to check
     * @return true if the dice can be placed in point, false otherwise
     * @throws IllegalArgumentException if the position is invalid, or the restriction or point is null
     */
    public boolean isDiceAllowed(Point point, DiceFace diceFace) {
        if (diceFace == null) throw new IllegalArgumentException(this.getClass().getCanonicalName()+": diceFace cannot be null!");

        return getRestriction(point).isDiceAllowed(diceFace);
    }

    /**
     * Get the restriction of a specific position
     * @param point the point of the schema, 0 based
     * @return the restriction in that cell
     */
    public CellRestriction getRestriction(Point point){
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

    /**
     * @return the name of this SchemaCard
     */
    public String getName() {
        return name;
    }
}














