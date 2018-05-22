package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.utils.Settings;

import java.util.ArrayList;

/**
 * @author Francesco Sgherzi
 * @since 09/05/2018
 * Holds the dices remaining from every turn.
 */
public class DiceHolder implements ImmutableCloneable{

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
    ArrayList<DiceFace> turnDices;

    if(turn > Settings.TURNS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Tryig to access a turn greater than " + Settings.TURNS);

    try{
      turnDices = turnHolder.get(turn);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Turn number is not in the ArrayList.");
    }

    try {
      removedDice = turnDices.remove(position);
    } catch (IndexOutOfBoundsException e){
      throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Position number is not in the ArrayList.");

    }

    return removedDice;
  }

  /**
   * Adds the dice at the selected turn.
   * @param turn The zero based turn number.
   * @param diceFace The diceFace to add.
   * @throws IllegalArgumentException When the turn is greater than `Settings.TURNS` or `diceFace` is null.
   */
  public void addDice(int turn, DiceFace diceFace){

    if(turn < 0) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Turn less than 0");
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

    if(turn < 0) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Turn less than 0.");
    if(turn >= Settings.TURNS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Trying to access a turn greater than " + Settings.TURNS);
    if(turnHolder.get(turn).isEmpty()){
      throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Trying to access an empty turn.");
    }

    // Uses `new DiceFace[0]` to infer the type
    return turnHolder.get(turn).toArray(new DiceFace[0]);
  }

  @Override
  public DiceHolderImmutable getImmutableInstance() {
    return new DiceHolderImmutable(this.turnHolder);
  }
}
