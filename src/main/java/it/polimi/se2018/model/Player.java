package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.Schema;

/**
 * A class to contain players data
 */
public class Player {

    private String name;
    private Schema schema;
    private int token;

    /**
     * @param name the name of the player; this must be unique across a match
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Get the current schema of a player
     * @return the schema associated with this player
     */
    public Schema getSchema() {
        return schema;
    }

    /**
     * Associate a player with his schema
     * Moreover sets the number of token of the player
     * @param schema the player's schema
     */
    public void setSchema(Schema schema) {
        if(schema==null)throw new IllegalArgumentException(getClass().getCanonicalName()+": schema cannot be null!");
        this.schema = schema;
        this.token = schema.getSchemaCardFace().getDifficulty();
    }

    /**
     * Get the player name
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
