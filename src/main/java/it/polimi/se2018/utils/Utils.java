package it.polimi.se2018.utils;

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
}
