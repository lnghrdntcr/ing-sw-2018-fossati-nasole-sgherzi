package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;

public class SchemaCardSelectedEvent extends Event {

    private int schemaCardId;

    protected SchemaCardSelectedEvent(String emitter, String player, int schemaCardId) {
        // TODO: as the selection will be given to the player schemaCardId will be a number in [0, 3]
        super(emitter, player);
        this.schemaCardId = schemaCardId;
    }
}
