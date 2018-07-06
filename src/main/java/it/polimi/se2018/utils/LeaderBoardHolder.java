package it.polimi.se2018.utils;

import org.json.JSONObject;

public class LeaderBoardHolder implements Comparable<LeaderBoardHolder> {

    private final String name;
    private final JSONObject scores;

    public LeaderBoardHolder(String name, JSONObject scores) {

        this.name = name;
        this.scores = scores;

    }

    @Override
    public int compareTo(LeaderBoardHolder o) {

        if (o == null) throw new NullPointerException("Leaderbord Holder cannot be null!");

        if (o.equals(this)) return 0;

        String oName = o.getName();

        int victories = scores.optInt("victories", 0);
        int losses = scores.optInt("losses", 0);
        int time = scores.optInt("totalTimePlayed", 0);


        int oVictories = o.getScores().optInt("victories", 0);
        int oLosses = o.getScores().optInt("losses", 0);
        int oTime = o.getScores().optInt("totalTimePlayed", 0);

        if (victories > oVictories) return 1;
        if (victories == oVictories) {
            if (losses < oLosses) return 1;
            if (losses == oLosses) {
                if (time > oTime) return 1;
                if (time == oTime) {
                    return name.compareTo(oName);
                }
            }
        }

        return -1;

    }

    public String getName() {
        return name;
    }

    public JSONObject getScores() {
        return scores;
    }
}
