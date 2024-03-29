package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model_view.PlayerImmutable;

/**
 * A class to contain players data
 */
public class Player implements ImmutableCloneable<PlayerImmutable>{

    private String name;
    private Schema schema;
    private int token;
    private PrivateObjective privateObjective;

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
     * Additionally sets the number of token of the player
     * @param schema the player's schema
     */
    public void setSchema(Schema schema) {

        if(schema==null)throw new IllegalArgumentException(getClass().getCanonicalName()+": schema cannot be null!");
        if(this.schema != null) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Trying to set a schema on an already set schema!");

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

    public PrivateObjective getPrivateObjective() {
      return privateObjective;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
      this.privateObjective = privateObjective;
    }

    /**
     * Computes the score of the current player's schema based on its privateObjective
     * @return the partial score of the player's schema
     */
    public int computeScoreFromPrivateObjective(){
        return this.privateObjective.computeScore(this.schema);
    }

    /**
     * @return the number of free space on the player schema
     */
    public int computeFreeSpaces(){
        return schema.computeFreeSpaces();
    }

    @Override
    public PlayerImmutable getImmutableInstance() {
      return new PlayerImmutable(this.name, this.token, this.privateObjective);
    }
}
