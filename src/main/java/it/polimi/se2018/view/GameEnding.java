package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import org.json.JSONObject;

import java.util.Set;

public abstract class GameEnding {
    private RemoteView view;

    private JSONObject globalLeaderBoard;

    public GameEnding(RemoteView view) {
        this.view = view;
    }

    public void handleEndGameEvent(EndGameEvent event){
        globalLeaderBoard = new JSONObject(event.getGlobalLeaderBoard());
    }

    public JSONObject getGlobalLeaderBoard() {
        return globalLeaderBoard;
    }

    public abstract void setActive();

    public abstract void setInactive();

    public RemoteView getView() {
        return view;
    }
}
