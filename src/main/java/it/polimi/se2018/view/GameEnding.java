package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.EndGameEvent;

public abstract class GameEnding {
    private RemoteView view;

    public GameEnding(RemoteView view) {
        this.view = view;
    }

    public abstract void handleEndGameEvent(EndGameEvent event);

    public abstract void setActive();

    public abstract void setInactive();

    public RemoteView getView() {
        return view;
    }
}
