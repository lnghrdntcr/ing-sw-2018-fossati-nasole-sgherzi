package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceBag;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.utils.Settings;

import java.util.ArrayList;
import java.util.Random;

/**
 * The DraftBoard that holds the dices drown in that turn.
 *
 * @author Francesco Sgherzi
 * @since 09/05/2018
 */
public class DraftBoard implements ImmutableCloneable{

    private DiceBag diceBag;
    private ArrayList<DiceFace> diceFaces = new ArrayList<>();

    public DraftBoard() {
        diceBag = new DiceBag();
    }

    /**
     * Draws the dices given a selected number of players.
     *
     * @param playerNumber The number of players in the game.
     * @throws IllegalArgumentException If the number of players is less than 2.
     */
    public void drawDices(int playerNumber) {

        if (playerNumber < Settings.MIN_NUM_PLAYERS)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": User number has to be at least 2.");
        if (playerNumber > Settings.MAX_NUM_PLAYERS)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": User number must, at most, be 4");

        int dices = 2 * playerNumber + 1;

        for (int i = 0; i < dices; i++) {
            diceFaces.add(diceBag.drawDice());
        }

    }

    /**
     * Removes and returns the dice selected by `position`.
     *
     * @param position The zero based position of the dice in the `diceFaces` ArrayList.
     * @return The diceFace removed from the the `diceFaces` ArrayList.
     * @throws IllegalArgumentException If position is less than zero or is greater than `diceFaces.size()`.
     */
    public DiceFace removeDice(int position) {

        DiceFace removedDice;

        if (position > this.getDiceNumber() || position < 0)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + "Trying to get an already taken dice or accessing a non existing position.");
        if (this.getDiceNumber() == 0)
            throw new IllegalStateException(this.getClass().getCanonicalName() + ": Trying to take a dice, but no dice is actually placed.");


        removedDice = diceFaces.remove(position);

        return removedDice;

    }

    /**
     * Adds the dice to the DraftBoard.
     *
     * @param diceFace The diceFace to add.
     * @throws IllegalArgumentException If the `diceFace` is null.
     */
    public void addDice(DiceFace diceFace) {

        if (diceFace == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null.");

        diceFaces.add(diceFace);

    }

    /**
     * Returns all the DiceFaces.
     *
     * @return All the diceFaces.
     * @throws IllegalStateException If there are no more dice to take.
     */
    public DiceFace[] getDices() {

        if (this.getDiceNumber() == 0)
            throw new IllegalStateException(this.getClass().getCanonicalName() + ": Cannot draw any more dice!");

        return diceFaces.toArray(new DiceFace[0]);

    }

    /**
     * @return The number in the DraftBoard
     */
    public int getDiceNumber() {
        return diceFaces.size();
    }

    /**
     * Put a dice back in to the DiceBag
     * @param index the index of the dice to put back
     */
    public void putBackDice(int index){
        diceBag.putBackDice(diceFaces.get(index));
        diceFaces.remove(index);
    }

    /**
     * Draws a single dice from the bag
     */
    public void drawSingleDice(){
        diceFaces.add(diceBag.drawDice());
    }

    /**
     * Returns a dice in a specific position
     * @param position position of the dice in the DraftBoard
     * @return the DiceFace
     * @throws IllegalArgumentException if position is invalid
     */
    public DiceFace getDiceFace(int position){
        if (position > this.getDiceNumber() || position < 0)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + "Trying to get an already taken dice or accessing a non existing position.");

        return diceFaces.get(position);
    }


    @Override
    public DraftBoardImmutable getImmutableInstance() {
        return new DraftBoardImmutable(this.getDices());
    }
}

