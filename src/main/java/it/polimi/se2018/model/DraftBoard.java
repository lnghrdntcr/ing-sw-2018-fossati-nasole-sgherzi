package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceBag;
import it.polimi.se2018.model.schema.DiceFace;

import java.util.ArrayList;

public class DraftBoard {

  private DiceBag diceBag;

  private ArrayList<DiceFace> diceFaces = new ArrayList<>();

  public void drawDices(int playerNumber){

  }

  public DiceFace removeDice(int position) {
    return null;
  }

  public void addDice(DiceFace diceFace){

  }

  public DiceFace [] getDices() {
    return diceFaces.toArray(new DiceFace[0]);
  }
}
