package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.utils.Event;

public class SchemaChangedEvent extends ModelEvent {
    Schema schema;

    public SchemaChangedEvent(String emitter, String player, Schema schema) {
        super(emitter, player);
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
