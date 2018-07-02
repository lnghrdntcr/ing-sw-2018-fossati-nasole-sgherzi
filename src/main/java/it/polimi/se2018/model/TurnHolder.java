package it.polimi.se2018.model;

import it.polimi.se2018.utils.Settings;

/**
 * Utility class to compute the round sequence
 *
 * @since 09/05/2018
 */
public class TurnHolder {
    private int round;
    private int players;
    private int currentPlayer;
    private int direction;
    private int firstInRound;
    private boolean toIncrement=false;

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
        firstInRound=0;
    }

    /**
     * Compute the next player from the actual
     *
     * @throws IllegalArgumentException if the game is ended
     */
    public void nextTurn() {
        if (round < Settings.TURNS) {
            currentPlayer = (currentPlayer+direction+players)%players;

            if(direction==0){
                direction=-1;
            }

            if(toIncrement) {
                round++;
                toIncrement=false;
            }

            if(currentPlayer == (firstInRound-1+players)%players && direction==1){
                direction=0;
            }else if(currentPlayer == firstInRound && direction == -1){
                direction=1;
                toIncrement=true;
                firstInRound=(firstInRound+1)%players;
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
        //if the direction is 1 we are going in the first direction, meaning we have not changed direction in this turn
        // yet, so basically the player is playing the first turn in this round

        return !toIncrement && (direction == 1 || direction==0);
    }

    @Override
    public String toString() {
        return "TurnHolder{" +
                "round=" + round +
                ", players=" + players +
                ", currentPlayer=" + currentPlayer +
                ", direction=" + direction +
                ", firstInRound=" + firstInRound +
                ", toIncrement=" + toIncrement +
                '}';
    }

}
