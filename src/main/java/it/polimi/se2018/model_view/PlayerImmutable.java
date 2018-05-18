package it.polimi.se2018.model_view;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.Schema;

/**
 * Player to be used in View
 * @author Francesco Sgherzi
 * @since 18/05/2018
 */
public class PlayerImmutable {

  private String name;
  private SchemaImmutable schema;
  private PrivateObjective privateObjective;

  public PlayerImmutable(String name, Schema schema, PrivateObjective privateObjective){

    this.name = name;
    this.schema = schema.getImmutableInstance();
    this.privateObjective = privateObjective;

  }

  public String getName() {
    return name;
  }

  public SchemaImmutable getSchema() {
    return schema;
  }

  public PrivateObjective getPrivateObjective() {
    return privateObjective;
  }
}
