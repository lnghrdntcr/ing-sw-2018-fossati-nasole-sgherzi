package it.polimi.se2018.utils;

/**
 * Settings for the game (constant values)
 */
public class Settings {
    /**
     * Width of the Schema
     */
    public static final int CARD_WIDTH = 5;
    /**
     * Height of the schema
     */
    public static final int CARD_HEIGHT = 4;
    /**
     * Result of 90 dices / {@link it.polimi.se2018.model.schema.GameColor}.values().length
     */
    public static final int MAX_DICE_PER_COLOR = 18;
    /**
     * Number of turns in a match
     */
    public static final int TURNS = 3;
    /**
     * Maximum number of players in a match
     */
    public static final int MAX_NUM_PLAYERS = 4;
    /**
     * Minimum number of players in a match
     */
    public static final int MIN_NUM_PLAYERS = 2;
    /**
     * Number of toolcards in a match
     */
    public static final int TOOLCARDS_N = 3;
    /**
     * Number of public objectives in a match
     */
    public static final int POBJECTIVES_N = 3;
    /**
     * Specifies the messages separator in the SocketString (Json) communication
     */
    public final static String SOCKET_EOM = "#$$EOM$$#";

    /**
     * The relative path in resources folder of the file that contains schema cards definitions
     */
    private final static String defaultSchemaCardDatabase = "schemaCards/schemaCardBase.scf";


    /**
     * Full path or relative path to a file that contains custom schema cards definitions
     */
    private static String customSchemaCardDatabase = "";

    private Settings() {
        //This constructor must be private because the class should not be instantiated from the outside
    }

    public static String getSchemaCardDatabase() {
        return customSchemaCardDatabase;
    }

    public static void setSchemaCardDatabase(String customSchemaCardPath) {
        if (customSchemaCardPath == null || customSchemaCardPath.equals("")) return;
        customSchemaCardDatabase = customSchemaCardPath;
    }

    public static String getDefaultSchemaCardDatabase() {
        return defaultSchemaCardDatabase;
    }
}
