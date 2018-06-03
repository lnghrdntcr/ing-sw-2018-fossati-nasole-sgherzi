package it.polimi.se2018.model.objectives;

import it.polimi.se2018.model.schema.Schema;

/**
 * A generic class to represent an objective
 * Computes a player score by a schema
 */
public abstract class Objective {
    public abstract int computeScore(Schema schema);
}
