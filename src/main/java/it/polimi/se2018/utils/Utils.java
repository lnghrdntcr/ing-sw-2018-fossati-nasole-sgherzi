package it.polimi.se2018.utils;

import org.json.JSONObject;

import java.awt.*;

public class Utils {

    public static Point decodePosition(JSONObject pointJson){
        return new Point(pointJson.getInt("x"), pointJson.getInt("y"));
    }
}
