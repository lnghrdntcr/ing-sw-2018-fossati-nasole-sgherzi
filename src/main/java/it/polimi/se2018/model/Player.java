package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.Schema;

/**
 * A class to contain players data
 */
public class Player {

    private String name;
    private Schema schema;

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
     * @param schema the player's schema
     */
    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    /**
     * Get the player name
     * @return the player's name
     */
    public String getName() {
        return name;
    }
}
