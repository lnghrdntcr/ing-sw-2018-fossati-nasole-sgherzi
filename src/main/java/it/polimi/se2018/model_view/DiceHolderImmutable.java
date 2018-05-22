package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;

import java.util.ArrayList;

/**
 * DiceHolder to be used in View
 * @author Francesco Sgherzi and a useful hand by the good ol'Insaniteaparty
 * @since 18/05/2018
 */
public class DiceHolderImmutable {

  private ArrayList<ArrayList<DiceFace>> turnHolder;

  public DiceHolderImmutable(ArrayList<ArrayList<DiceFace>> turnHolder){
    this.turnHolder = turnHolder;
  }

  public ArrayList<ArrayList<DiceFace>> getDiceFaces() {
    return turnHolder;
  }
}
