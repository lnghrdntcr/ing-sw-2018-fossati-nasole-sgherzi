package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.controller.controllerEvent.TimeoutCommunicationEvent;
import it.polimi.se2018.model.modelEvent.PlayerChangedEvent;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;

public abstract class SelectSchemaCardFace {
    private RemoteView view;
    private PrivateObjective privateObjective;
    private int secondsRemaining;


    public SelectSchemaCardFace(RemoteView view) {
        this.view = view;
    }

    protected final void selectFace(int index, Side side){
       view.sendEventToController(new SchemaCardSelectedEvent(getClass().getCanonicalName(), view.getPlayer(), index, side));
       view.activateGameTable();
    }

    public void handlePlayerCanged(PlayerChangedEvent e){
        this. privateObjective = e.getPlayerImmutable().getPrivateObjective();
        renderPrivateObjective(privateObjective);
    }

    final public void handleTimeoutCommunication(TimeoutCommunicationEvent event){
        this.secondsRemaining = event.getTimeout();
    }


    //updates
    public abstract void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event);

    public abstract void setActive();

    public abstract void setInactive();

    public abstract void renderPrivateObjective(PrivateObjective privateObjective);


}
