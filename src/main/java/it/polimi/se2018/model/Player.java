package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.Schema;

public class Player {

  private String name;
  private String id;
  private Schema schema;
  private PrivateObjective privateObjective;

  public Player(String name, String id){
    this.name = name;
    this.id = id;
  }

  public Schema getSchema() {
    return schema;
  }

  public void setSchema(Schema schema) {
    this.schema = schema;
  }

  public PrivateObjective getPrivateObjective() {
    return privateObjective;
  }

  public void setPrivateObjective(PrivateObjective privateObjective) {
    this.privateObjective = privateObjective;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
