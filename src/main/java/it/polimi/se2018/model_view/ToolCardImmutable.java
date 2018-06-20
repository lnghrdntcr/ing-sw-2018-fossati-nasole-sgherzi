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

  public int getNeededTokens() {
    return token > 0 ? 2 : 1;
  }

  @Override
  public String toString() {
    return "ToolCardImmutable{" +
        "name='" + name + '\'' +
        ", token=" + token +
        '}';
  }
}
