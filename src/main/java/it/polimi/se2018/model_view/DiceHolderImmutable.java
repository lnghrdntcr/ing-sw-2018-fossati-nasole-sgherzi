package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;

import java.util.ArrayList;

/**
 * DiceHolder to be used in View
 * @author Francesco Sgherzi
 * @since 18/05/2018
 */
// TODO: Fix this class. It is supposed to get all dices per turn, but the DiceHolder class throws an exception ...
// if trying to access a turn for which no dices have been placed.
public class DiceHolderImmutable {

  private ArrayList<ArrayList<DiceFace>> turnHolder;

  public DiceHolderImmutable(ArrayList<ArrayList<DiceFace>> turnHolder){
    this.turnHolder = turnHolder;
  }

  public ArrayList<ArrayList<DiceFace>> getDiceFaces() {
    return turnHolder;
  }
}
