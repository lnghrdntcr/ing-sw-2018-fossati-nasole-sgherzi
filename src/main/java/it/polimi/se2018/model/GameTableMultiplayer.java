package it.polimi.se2018.model;

import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.schema.DiceFace;

import java.awt.*;
import java.util.NoSuchElementException;

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
     * @param position zero based position of the card. 0 <= position < 3
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
     * @param position zero based position of the card. 0 <= position < 3
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
     * End the current turn
     */
    //Turn stuff
    public void nextTurn(){
        turnHolder.nextTurn();
    }



}
