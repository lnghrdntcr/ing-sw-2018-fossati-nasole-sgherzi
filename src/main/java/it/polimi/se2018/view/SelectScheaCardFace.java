package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;

public abstract class SelectScheaCardFace {
    private RemoteView view;

    private SelectScheaCardFace(RemoteView view) {
        this.view = view;
    }

    protected final void selectSchemaCardFace(int index, Side side){
       view.sendEventToController(new SchemaCardSelectedEvent(getClass().getCanonicalName(), view.getPlayer(), index, side));
    }

    public abstract void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event);
}
