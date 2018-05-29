package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;

public class SchemaCardSelectedEvent extends Event {

    private int schemaCardId;

    protected SchemaCardSelectedEvent(String emitter, String player, int schemaCardId) {
        super(emitter, player);
        if (schemaCardId >= 0 && schemaCardId < 4) {
            this.schemaCardId = schemaCardId;
        } else
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": number must be between 0 and 3.");
    }

    public int getSchemaCardId() {
        return schemaCardId;
    }
}
