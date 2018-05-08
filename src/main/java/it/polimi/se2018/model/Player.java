package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;

import java.awt.*;

public class Player {

  private String name;
  private String id;
  private Schema schema;
  private PrivateObjective privateObjective;

  public Player(String name, String id){

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
