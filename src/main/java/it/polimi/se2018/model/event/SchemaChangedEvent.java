package it.polimi.se2018.model.event;

import it.polimi.se2018.model.schema.Schema;

public class SchemaChangedEvent extends Event {
    Schema schema;

    public SchemaChangedEvent(String emitter, String player, Schema schema) {
        super(emitter, player);
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
