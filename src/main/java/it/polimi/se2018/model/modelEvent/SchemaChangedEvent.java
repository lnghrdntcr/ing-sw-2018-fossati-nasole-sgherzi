package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * An event to inform that a player's schema has been changed
 */
public class SchemaChangedEvent extends ModelEvent {
    Schema schema;

    public SchemaChangedEvent(String emitter, String player, Schema schema) {
        super(emitter, player);
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleShcemaChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
