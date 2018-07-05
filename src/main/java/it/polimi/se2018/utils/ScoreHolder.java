package it.polimi.se2018.utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Holds the point of the given player and how it obtainded them.
 * @since 29/06/2018
 */
public class ScoreHolder implements Comparable<ScoreHolder>, Serializable {

    private String player;
    private int privateObjectivePoints;
    private int tokenPoints;
    private int orderInFinalRound;
    private int publicObjectivePoints;
    private int missing;


    public ScoreHolder(String player, int privateObjectivePoints, int publicObjectivePoints, int tokenPoints, int missing, int orderInFinalRound) {

        if(
            tokenPoints < 0 ||
            privateObjectivePoints < 0 ||
            publicObjectivePoints < 0 ||
            missing < 0 ||
            orderInFinalRound < 0
            ) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": None of the attributes can be negative");

        this.player = player;
        this.privateObjectivePoints = privateObjectivePoints;
        this.tokenPoints = tokenPoints;
        this.orderInFinalRound = orderInFinalRound;
        this.publicObjectivePoints = publicObjectivePoints;
        this.missing = missing;

    }

    public ScoreHolder(JSONObject obj) {
        this(obj.getString("playerName"), obj.getInt("privateObjectivePoints"), obj.getInt("publicObjectivePoints"), obj.getInt("tokenPoints"), obj.getInt("missing"), obj.getInt("orderInFinalRound"));
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

    @Override
    public String toString() {
        return "ScoreHolder{" +
            "player='" + player + '\'' +
            ", privateObjectivePoints=" + privateObjectivePoints +
            ", tokenPoints=" + tokenPoints +
            ", orderInFinalRound=" + orderInFinalRound +
            ", publicObjectivePoints=" + publicObjectivePoints +
            ", missing=" + missing +
            '}';
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
     * @return 1 if the actual ScoreHolder (this) has to come first in the leaderboard, -1 otherwise. To comply with the specification, compareTo must return 0 if the two ScoreHolders are the same object.
     */
    @Override
    public int compareTo(ScoreHolder o) {

        if(o == null) throw new NullPointerException();

        if(this.equals(o)) return 0;

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


    public int getPrivateObjectivePoints() {
        return privateObjectivePoints;
    }

    public int getMissing() {
        return missing;
    }
}
