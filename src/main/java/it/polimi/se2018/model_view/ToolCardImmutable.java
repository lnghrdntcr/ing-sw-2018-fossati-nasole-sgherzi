package it.polimi.se2018.model_view;

import java.io.Serializable;

/**
 * Toolcard to be used in View
 * @since 18/05/2018
 */
public class ToolCardImmutable implements Serializable {

  private String name;
  private int token;

  public ToolCardImmutable(String name, int token){
    this.name = name;
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public int getToken() {
    return token;
  }
}
