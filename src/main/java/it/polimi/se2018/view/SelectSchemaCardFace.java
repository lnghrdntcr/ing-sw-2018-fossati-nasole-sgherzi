package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.controller.controllerEvent.LogEvent;
import it.polimi.se2018.controller.controllerEvent.TimeoutCommunicationEvent;
import it.polimi.se2018.model.modelEvent.PlayerChangedEvent;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;

import java.util.ArrayList;

public abstract class SelectSchemaCardFace {
    private RemoteView view;
    private PrivateObjective privateObjective;
    private int secondsRemaining;
    private ArrayList<LogEvent> logs = new ArrayList<>();


    public SelectSchemaCardFace(RemoteView view) {
        this.view = view;
    }

    protected final void selectFace(int index, Side side) {
        view.sendEventToController(new SchemaCardSelectedEvent(getClass().getCanonicalName(), view.getPlayer(), view.getPlayer(), index, side));
    }

    public void handlePlayerCanged(PlayerChangedEvent e) {
        if (e.getPlayerName().equals(view.getPlayer())) {
            this.privateObjective = e.getPlayerImmutable().getPrivateObjective();
            renderPrivateObjective(privateObjective);
        }
    }

    final public void handleTimeoutCommunication(TimeoutCommunicationEvent event) {
        this.secondsRemaining = event.getTimeout();
        renderSecondsRemaining();
    }

    final public void handleLogEvent(LogEvent event) {
        // TODO: here
        logs.add(event);
        renderEventLog();
    }

    public ArrayList<LogEvent> getPastEvents() {
        return logs;
    }

    protected abstract void renderSecondsRemaining();


    //updates

    public abstract void renderEventLog();

    public abstract void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event);

    public abstract void setActive();

    public abstract void setInactive();

    public abstract void renderPrivateObjective(PrivateObjective privateObjective);

    public int getSecondsRemaining() {
        return secondsRemaining;
    }
}
