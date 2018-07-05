package it.polimi.se2018.utils;

/**
 * Settings for the game (constant values)
 */
public class Settings {
    private Settings() {
        //This constructor must be private because the class should not be instantiated from the outside
    }

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
    public static final int TURNS = 1;
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

    private static String schemaCardDatabase ="./gameData/resources/schemaCards/schemaCardBase.scf";

    public static void setSchemaCardDatabase(String customSchemaCardPath) {
        if(customSchemaCardPath==null || customSchemaCardPath.equals("")) return;
        schemaCardDatabase = customSchemaCardPath;
    }

    public static String getSchemaCardDatabase(){
        return schemaCardDatabase;
    }

    public final static String SOCKET_EOM="#$$EOM$$#";
}
