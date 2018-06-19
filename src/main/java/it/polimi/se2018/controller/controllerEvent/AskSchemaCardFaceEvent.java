package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * Event to ask the player to select a SchemaCardFace, at the beginning of the game
 */
public class AskSchemaCardFaceEvent extends ControllerEvent {
    SchemaCard[] schemas;

    public AskSchemaCardFaceEvent(String emitter, String receiver, String player, SchemaCard[] schemas) {
        super(emitter, player, receiver);
        this.schemas = schemas;
    }

    public SchemaCard[] getSchemas() {
        return schemas;
    }

    @Override
    public void visit(GameTable gameTable) {

    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {
        selectSchemaCardFace.showSchemaCardFaceSelection(this);
    }
}
