package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.schema_card.SchemaCard;

public class AskSchemaCardFaceEvent extends ControllerEvent {
    SchemaCard[] schemas;

    public AskSchemaCardFaceEvent(String emitter, String player, SchemaCard[] schemas) {
        super(emitter, player);
        this.schemas=schemas;
    }

    public SchemaCard[] getSchemas() {
        return schemas;
    }
}
