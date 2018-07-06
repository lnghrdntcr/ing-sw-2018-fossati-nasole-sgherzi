package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.utils.LeaderBoardHolder;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public abstract class GameEnding {
    private RemoteView view;

    private JSONObject globalLeaderBoard;

    private List<LeaderBoardHolder> globalLeaderboardUnpacked = new ArrayList<>();

    public GameEnding(RemoteView view) {
        this.view = view;
    }

    public void handleEndGameEvent(EndGameEvent event) {

        List<LeaderBoardHolder> jsonLeaderboard = new ArrayList<>();
        globalLeaderBoard = new JSONObject(event.getGlobalLeaderBoard());

        Set<String> keys = globalLeaderBoard.keySet();

        for (String key : keys) {
            jsonLeaderboard.add(new LeaderBoardHolder(key, globalLeaderBoard.getJSONObject(key)));
        }

        Collections.sort(jsonLeaderboard);
        Collections.reverse(jsonLeaderboard);

        globalLeaderboardUnpacked.addAll(jsonLeaderboard);


    }

    public List<LeaderBoardHolder> getGlobalLeaderboardUnpacked() {
        return globalLeaderboardUnpacked;
    }

    public abstract void setActive();

    public abstract void setInactive();

    public RemoteView getView() {
        return view;
    }

}
