package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;

public abstract class SelectSchemaCardFace {
    private RemoteView view;

    public SelectSchemaCardFace(RemoteView view) {
        this.view = view;
    }

    // TODO: React to player changes (Show privateObjectiveCard) and timeout
    protected final void selectSchemaCardFace(int index, Side side){
       view.sendEventToController(new SchemaCardSelectedEvent(getClass().getCanonicalName(), view.getPlayer(), index, side));
       view.activateGameTable();
    }

    public abstract void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event);
}
