package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.controller.tool.Tool;

public class GameTable {

  private Player currentPlayer;
  private PublicObjective[] publicObjectives = new PublicObjective[3];
  private Player[] players;
  private Tool[] toolCards = new Tool[3];
  private DraftBoard draftBoard;
  private DiceHolder diceHolder;

  public GameTable(){

  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public Player getPlayerById(String id){
    return null;
  }

  public Tool getToolCardByPosition(int position){
    return null;
  }

  public PublicObjective getPublicObjectiveCardByPosition(int position){
    return null;
  }

  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

}
