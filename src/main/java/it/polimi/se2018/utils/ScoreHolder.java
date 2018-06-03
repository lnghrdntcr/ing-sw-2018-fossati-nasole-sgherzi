package it.polimi.se2018.utils;

/**
 * Holds the point of the given player and how it obtainded them.
 * @since 29/06/2018
 */
public class ScoreHolder implements Comparable<ScoreHolder> {

    private String player;
    private int privateObjectivePoints;
    private int tokenPoints;
    private int orderInFinalRound;
    private int missing;


    private int publicObjectivePoints;


    public ScoreHolder(String player, int privateObjectivePoints, int publicObjectivePoints, int tokenPoints, int missing, int orderInFinalRound) {

        this.player = player;
        this.privateObjectivePoints = privateObjectivePoints;
        this.tokenPoints = tokenPoints;
        this.orderInFinalRound = orderInFinalRound;
        this.publicObjectivePoints = publicObjectivePoints;
        this.missing = missing;

    }

    /**
     * Computes the score of the player, given the informations provided above.
     * @return The total score of the player.
     */
    public int getTotalScore(){
        return
            this.privateObjectivePoints +
            this.publicObjectivePoints +
            this.tokenPoints -
            this.missing;
    }

    public int getTokenPoints() {
        return tokenPoints;
    }

    public int getOrderInFinalRound() {
        return orderInFinalRound;
    }

    public int getPublicObjectivePoints() {
        return publicObjectivePoints;
    }

    public String getPlayerName() {
        return player;
    }

    /**
     * Compares 2 ScoreHolders, according to the game logic.
     * @param o The ScoreHolder to compare to.
     * @return 1 if the actual ScoreHolder (this) has to come first in the leaderboard, -1 otherwise.
     */
    @Override
    public int compareTo(ScoreHolder o) {

        if(this.getTotalScore() > o.getTotalScore()) return 1;
        if(this.getTotalScore() == o.getTotalScore()){
            if(this.getPublicObjectivePoints() > o.getPublicObjectivePoints()) return 1;
            if(this.getPublicObjectivePoints() == o.getPublicObjectivePoints()) {
                if(this.getTokenPoints() > o.getTokenPoints()) return 1;
                if(this.getTokenPoints() == o.getTokenPoints()) {
                    if(this.getOrderInFinalRound() > o.getOrderInFinalRound()) return 1;
                }
            }
        }
        return -1;
    }
}
