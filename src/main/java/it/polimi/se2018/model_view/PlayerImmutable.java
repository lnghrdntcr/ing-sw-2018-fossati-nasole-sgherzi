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
  private int token;
  private PrivateObjective privateObjective;

  public PlayerImmutable(String name, int token, PrivateObjective privateObjective){

    this.name = name;
    this.token =token;
    this.privateObjective = privateObjective;

  }

  public String getName() {
    return name;
  }


  public PrivateObjective getPrivateObjective() {
    return privateObjective;
  }

  public int getToken() {
    return token;
  }
}
