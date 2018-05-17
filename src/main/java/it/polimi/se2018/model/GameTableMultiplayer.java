package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;

import java.awt.*;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * The main model entry point for multiplayer
 */
public class GameTableMultiplayer {

    final private PublicObjective[] publicObjectives;
    final private Player[] players;
    final private Tool[] toolCards;
    final private DraftBoard draftBoard;
    final private DiceHolder diceHolder;
    final private TurnHolder turnHolder;

    public GameTableMultiplayer(PublicObjective[] publicObjectives, Player[] players, Tool[] toolCards) {
        this.publicObjectives = publicObjectives;
        this.players = players;
        turnHolder = new TurnHolder(players.length);
        this.toolCards = toolCards;
        draftBoard = new DraftBoard();
        diceHolder = new DiceHolder();
    }


    /**
     * @return who's playing
     */
    private Player getCurrentPlayer() {
        return players[turnHolder.getCurrentPlayer()];
    }

    /**
     * Return a player by its name
     *
     * @param name the name of the player
     * @return the player with the given name
     * @throws NoSuchElementException if no player with the given name exists
     */
    private Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the Tool card by position
     *
     * @param position zero based position of the card. Position is grater than or equal to zero and lesser than 3
     * @return the Tool card
     * @throws IllegalArgumentException if position outside of range
     */
    public Tool getToolCardByPosition(int position) {
        if (position < 0 || position >= toolCards.length)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": position must be >=0 and <3, given " + position);
        return toolCards[position];
    }

    /**
     * Returns the PublicObjectiveCard by position
     *
     * @param position zero based position of the card. Position is grater than or equal to zero and lesser than 3
     * @return the PublicObjectiveCard
     * @throws IllegalArgumentException if position outside of range
     */
    public PublicObjective getPublicObjectiveCardByPosition(int position) {
        if (position < 0 || position >= toolCards.length)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": position must be >=0 and <3, given " + position);
        return publicObjectives[position];
    }

    //Player stuff


    /**
     * Get the number of token of the player
     *
     * @param playerName the player's name
     * @return the number of token of player
     * @throws NoSuchElementException if player does not exist
     */
    public int getPlayerToken(String playerName) {
        return getPlayerByName(playerName).getToken();
    }

    /**
     * Perform the action to place a dice, without checking any condition
     *
     * @param playerName the players that perform the action
     * @param diceIndex  index of the dice in diceholder
     * @param point      where to put the dice in the Schema
     * @throws IllegalStateException if the cell is already taken
     */
    public void placeDice(String playerName, int diceIndex, Point point) {
        Player p = getPlayerByName(playerName);
        DiceFace df = draftBoard.removeDice(diceIndex);
        p.getSchema().setDiceFace(point, df);
    }


    // Tool card stuff

    /**
     * Performs the transaction of token to use a toolcard
     *
     * @param player   the player who uses the toolcard
     * @param toolCard the toolcard used
     */
    public void updateToken(String player, Tool toolCard) {
        Player p = getPlayerByName(player);
        int neededToken = toolCard.getNeededTokens();
        p.setToken(p.getToken() - neededToken);
        toolCard.addToken(neededToken);
    }

    /**
     * Increases or decreases a dice value
     * @param diceIndex the index of the dice in the DraftBoard
     * @param direction 1 to increase, -1 to decrease
     * @throws IllegalArgumentException if diceIndex is not equal to -1 and direction is not equal to -1
     * @throws IllegalStateException if increasing a 6 dice or decreasing a 1 dice
     */
    public void increaseDecreaseDice(int diceIndex, int direction){
        if(direction != 1 && direction!=-1) throw new IllegalArgumentException(getClass().getCanonicalName()+": direction must be +1 or -1");

        DiceFace df = draftBoard.getDices()[diceIndex];
        if(direction == 1 && df.getNumber() == 6 ){
            throw new IllegalStateException(getClass().getCanonicalName()+": cannot increase a 6 dice");
        }

        if(direction == -1 && df.getNumber() == 1 ){
            throw new IllegalStateException(getClass().getCanonicalName()+": cannot decrease a 1 dice");
        }

        draftBoard.removeDice(diceIndex);
        draftBoard.addDice(new DiceFace(df.getColor(), df.getNumber()+direction));
    }

    /**
     * Swap a DiceFace from the DraftBoard with a DiceFace from the DiceHolder area
     * @param draftIndex index of the dice in the DraftBoard
     * @param turn the turn from wich swap the dice
     * @param turnDiceIndex the index in the turn of the dice to swipe
     */
    public void swapDraftDiceWithHolder(int draftIndex, int turn, int turnDiceIndex){
        DiceFace oldDfDraft = draftBoard.removeDice(draftIndex);
        DiceFace oldDfHolder = diceHolder.removeDice(turn, turnDiceIndex);

        draftBoard.addDice(oldDfHolder);
        diceHolder.addDice(turn, oldDfDraft);
    }

    /**
     * Redraw a dice (change its number but not its color)
     * @param index the index of the dice to redraw
     * @return the DiceFace just redrawn
     */
    public DiceFace redrawDice(int index){
        DiceFace df = draftBoard.removeDice(index);
        df  = new DiceFace(df.getColor(), new Random().nextInt(6)+1);
        draftBoard.addDice(df);
        return df;
    }

    /**
     * Redraw all dices (change their number but not their color)
     */
    public void redrawAllDice(){
        for(int i=0; i<draftBoard.getDiceNumber(); i++){
            redrawDice(0);
        }
    }

    /**
     * Flip a dice: 6 becomes 1, 5 becomes 2, 4 becomes 3 etc.
     * @param index the index of the dice to flip in the DraftBoard
     */
    public void flipDice(int index){
        DiceFace df = draftBoard.removeDice(index);
        df = new DiceFace(df.getColor(), 7-df.getNumber());
        draftBoard.addDice(df);
    }

    /**
     * Put a single dice back in the DiceBag and then redraws a new dice
     * @param index index of the dice to put back in the DiceBag
     * @return the dice just drawn
     */
    public DiceFace putBackAndRedrawDice(int index){
        draftBoard.putBackDice(index);
        draftBoard.drawSingleDice();
        return draftBoard.getDiceFace(draftBoard.getDiceNumber()-1);
    }

    /**
     * Change the number of a dice to a specific number
     * @param index index of the dice in the DraftBoard to change
     * @param number new value to assign to the dice. Must be between 1 and 6.
     */
    public void changeDiceNumber(int index, int number){
        draftBoard.addDice(new DiceFace(draftBoard.removeDice(index).getColor(), number));
    }

    /**
     * Move a dice in the schema of a player from position source to destination
     * @param player the player to modify
     * @param source the source position where to pick up the dice
     * @param destination the destination where to put the dice
     */
    public void moveDice(String player, Point source, Point destination){
        Player p = getPlayerByName(player);
        p.getSchema().setDiceFace(destination, p.getSchema().removeDiceFace(source));
    }

    //Turn stuff

    /**
     * End the current turn
     */
    public void nextTurn(){
        turnHolder.nextTurn();
    }







}
