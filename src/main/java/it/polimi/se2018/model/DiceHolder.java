package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceBag;
import it.polimi.se2018.model.schema.DiceFace;

import java.util.ArrayList;

public class DiceHolder {


  private ArrayList<ArrayList<DiceFace>> turnHolder = new ArrayList<>();

  public DiceFace removeDice(int turn, int position){
    return null;
  }

  public void addDice(int turn, DiceFace diceFace){

  }

  public DiceFace[] getTurnDices(int turn) {
    // Uses new DiceFace[0] to determine the type
    return turnHolder.get(turn).toArray(new DiceFace[0]);
  }

}
