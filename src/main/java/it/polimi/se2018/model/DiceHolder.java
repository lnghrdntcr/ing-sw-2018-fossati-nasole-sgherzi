package it.polimi.se2018.model;

import com.sun.istack.internal.NotNull;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Settings;

import java.util.ArrayList;

/**
 * @author Francesco Sgherzi
 * @since 09/05/2018
 * Holds the dices remaining from every turn.
 */
public class DiceHolder {

  private ArrayList<ArrayList<DiceFace>> turnHolder = new ArrayList<>();

  public DiceHolder(){
    for(int i = 0; i < 10; i++){
      turnHolder.add(new ArrayList<>());
    }
  }

  /**
   * Removes and returns the dice selected by `turn` and `position`.
   * @param turn The zero based turn number.
   * @param position The zero based position of the dice in the turn ArrayList.
   * @return The selected DiceFace, removing it from the turn ArrayList.
   * @throws IllegalArgumentException When the turn is greater than `Settings.TURNS` or when the index of the turn or the position is not in the ArrayList.
   */
  public DiceFace removeDice(int turn, int position){

    DiceFace removedDice;

    if(turn > Settings.TURNS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Tryig to access a turn greater than " + Settings.TURNS);

    try{
      removedDice = turnHolder.get(turn).remove(position);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Position or turn number are not in the ArrayList");
    }

    return removedDice;
  }

  /**
   * Adds the dice at the selected turn.
   * @param turn The zero based turn number.
   * @param diceFace The diceFace to add.
   * @throws IllegalArgumentException When the turn is greater than `Settings.TURNS` or `diceFace` is null.
   */
  public void addDice(int turn, @NotNull DiceFace diceFace){

    if(turn > Settings.TURNS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Tryig to access a turn greater than " + Settings.TURNS);
    if(diceFace == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null.");

    turnHolder.get(turn).add(diceFace);

  }


  /**
   * Returns the turn diceFaces selected by `turn`, as an array.
   * @param turn The zero based turn number.
   * @return All the diceFaces of that turn.
   * @throws IllegalArgumentException When the turn is greater than `Settings.TURNS`
   */
  public DiceFace[] getTurnDices(int turn) {
    // Uses new DiceFace[0] to infer the type
    if(turn > Settings.TURNS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Tryig to access a turn greater than " + Settings.TURNS);
    return turnHolder.get(turn).toArray(new DiceFace[0]);
  }

}
