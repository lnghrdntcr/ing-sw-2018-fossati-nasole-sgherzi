package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceBag;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Settings;

import java.util.ArrayList;
import java.util.Random;

/**
 * The DraftBoard that holds the dices drown in that turn.
 * @author Francesco Sgherzi
 * @since 09/05/2018
 */
public class DraftBoard {

  private DiceBag diceBag;
  private Random random;
  private ArrayList<DiceFace> diceFaces = new ArrayList<>();

  public DraftBoard(){
    diceBag = new DiceBag();
    random = new Random();
  }

  /**
   * Draws the dices given a selected number of players.
   * @param playerNumber The number of players in the game.
   * @throws IllegalArgumentException If the number of players is less than 2.
   */
  public void drawDices(int playerNumber){

    if(playerNumber < Settings.MIN_NUM_PLAYERS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": User number has to be at least 2.");
    if(playerNumber > Settings.MAX_NUM_PLAYERS) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": User number must, at most, be 4");

    int dices = 2 * playerNumber + 1;

    for(int i = 0; i < dices; i++){
      diceFaces.add(diceBag.drawDice());
    }

  }

  /**
   * Removes and returns the dice selected by `position`.
   * @param position The zero based position of the dice in the `diceFaces` ArrayList.
   * @return The diceFace removed from the the `diceFaces` ArrayList.
   * @throws IllegalArgumentException If position is less than zero or is greater than `diceFaces.size()`.
   */
  public DiceFace removeDice(int position) {

    DiceFace removedDice;

    if(position > this.getDiceNumber() || position < 0) throw new IllegalArgumentException(this.getClass().getCanonicalName() + "Trying to get an already taken dice or accessing a non existing position.");
    if(this.getDiceNumber() == 0) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Trying to take a dice, but no dice is actually placed.");

    try{
      removedDice = diceFaces.remove(position);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Position is not in the ArrayList");
    }

    return removedDice;

  }

  /**
   * Adds the dice into the `diceFaces` ArrayList.
   * @param diceFace The diceFace to add.
   * @throws IllegalArgumentException If the `diceFace` is null.
   */
  public void addDice(DiceFace diceFace){

    if(diceFace == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null.");

    diceFaces.add(diceFace);

  }

  /**
   * Returns the turn diceFaces, as an array.
   * @return All the diceFaces of that turn.
   * @throws IllegalStateException If there are no more dice to take.
   */
  public DiceFace[] getDices() {

    if(this.getDiceNumber() == 0) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Cannot draw any more dice!");

    return diceFaces.toArray(new DiceFace[0]);

  }

  /**
   * @return The size of the `diceFaces` ArrayList.
   */
  public int getDiceNumber(){
    return diceFaces.size();
  }

}

