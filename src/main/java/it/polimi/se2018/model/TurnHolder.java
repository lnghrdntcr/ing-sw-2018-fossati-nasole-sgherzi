package it.polimi.se2018.model;

import it.polimi.se2018.utils.Settings;

/**
 * Utility class to compute the round sequence
 *
 * @author Nicola Fossati
 * @since 09/05/2018
 */
public class TurnHolder {
    private int round;
    private int players;
    private int currentPlayer;
    private int direction;

    /**
     * @param players the number of players that take part to the match
     * @throws IllegalArgumentException if players are less than 2 or players are more than 4.
     */
    public TurnHolder(int players) {
        if (players < Settings.MIN_NUM_PLAYERS || players > Settings.MAX_NUM_PLAYERS)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": players must be between 2 and 4!");
        this.players = players;
        currentPlayer = 0;
        direction = 1;
        round = 0;
    }

    /**
     * Compute the next player from the actual
     *
     * @throws IllegalArgumentException if the game is ended
     */
    public void nextTurn() {
        if (round < Settings.TURNS) {
            currentPlayer += direction;
            if (currentPlayer >= players) {
                currentPlayer = players - 1;
                direction = -1;
            } else if (currentPlayer < 0) {
                currentPlayer = 0;
                direction = 1;
                round++;
            }
        } else {
            throw new IllegalStateException(getClass().getCanonicalName()+": the game is already ended!");
        }
    }

    /**
     * Check if the game is ended
     *
     * @return true if the game is ended, false otherwise
     */
    public boolean isGameEnded() {
        return !(round < Settings.TURNS);
    }

    /**
     * get the actual Round
     *
     * @return the actual round zero based (0-Settings.TURNS) or 10 if the game is ended
     */
    public int getRound() {
        return round;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if this is the first turn in the current round for the current player
     *
     * @return true if it's the first turn, false otherwise
     */
    public boolean isFirstTurnInRound() {
        //if the direction is 1 we are going in the first direction, meaning we have not changed direction in thi turn
        // yet, so basically the player is playing the first turn in this round
        return direction == 1;
    }
}
