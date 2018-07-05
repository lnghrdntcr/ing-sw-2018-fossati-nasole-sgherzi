package it.polimi.se2018.utils;

import it.polimi.se2018.model.schema.DiceFace;
import org.json.JSONObject;

import java.awt.*;

public class Utils {

    /**
     * Return a {@link Point} based on it's {@link JSONObject} representation
     *
     * @param pointJson a {@link JSONObject} that represent a Point: it must have an int x and an int y
     * @return a new {@link Point} that matches the input
     */
    public static Point decodePosition(JSONObject pointJson) {
        return new Point(pointJson.getInt("x"), pointJson.getInt("y"));
    }

    public static String decodePosition(Point point){
        return point.x + ", "+ point.y;
    }

    public static String decodeCardinalNumber(int number){
        return (number == 1 ? "1st " : (number == 2 ? "2nd " : (number == 3 ? "3rd" : number + "th")));
    }

    public static String decodeDice(DiceFace df){
        return df.getColor().toString().toLowerCase() + " " + df.getNumber() + " ";
    }

}
