package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Settings;

import java.util.ArrayList;

/**
 * DiceHolder to be used in View
 * @author Francesco Sgherzi and a useful hand by the good ol'Insaniteaparty
 * @since 18/05/2018
 */
public class DiceHolderImmutable {

  private DiceFace[][] turnHolder;

  public DiceHolderImmutable(DiceFace[][] turnHolder){

    if(turnHolder == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": turnHolder Cannot be null.");
    this.turnHolder = turnHolder;

  }

  public DiceFace[] getDiceFaces(int turn) {

    if(turn < 0 || turn >= this.turnHolder.length) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Trying to access an illegal turn");

    return this.turnHolder[turn];

  }

}
