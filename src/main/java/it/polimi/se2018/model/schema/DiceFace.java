package it.polimi.se2018.model.schema;

import it.polimi.se2018.view.gui.Dice;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Holds the logic for the dice to be placed in the schema card or the scoreboard
 * @since 09/05/2018
 */
public class DiceFace implements Serializable {

    private GameColor color;
    private int number;

    /**
     * @param color  The color of the Dice Face.
     * @param number The number of the Dice Face.
     * @throws IllegalArgumentException if the number is less than 1 or greater than 6.
     */
    public DiceFace(GameColor color, int number) {
        if (number < 1 || number > 6){
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Number must be greater than zero and less than 7, not "+number);
        }
        if (color == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Color cannot be null.");
        this.color = color;
        this.number = number;
    }

    /**
     * Decode a {@link JSONObject} in order to recreate a {@link DiceFace} object
     * @param jsonObject the JSON to decode
     * @return the DiceFace represented by the JSON
     */
    public static DiceFace fromJson(JSONObject jsonObject){

        GameColor gc = null;
        for(GameColor tempColor: GameColor.values()){
            if(tempColor.toString().toUpperCase().equals(jsonObject.getString("color"))){
                gc=tempColor;
            }
        }

        return new DiceFace(gc, jsonObject.getInt("number"));
    }

    /**
     * @return the color of the dice
     */
    public GameColor getColor() {
        return color;
    }

    /**
     * @return the number of the dice
     */
    public int getNumber() {
        return number;
    }

    /**
     * Check if a dice is similar to this dice.
     * @param diceFace the other DiceFace to compare to this one.
     * @return true if color or (inclusively) number is equal, false otherwise.
     * @throws IllegalArgumentException if the diceFace is null.
     * @author Nicola Fossati
     */
    public boolean isSimilar(DiceFace diceFace) {

        if(diceFace == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null.");

        return (this.color.equals(diceFace.getColor()) || this.number == diceFace.getNumber());

    }

    public JSONObject toJSON() {
        return new JSONObject(this);
    }
}
