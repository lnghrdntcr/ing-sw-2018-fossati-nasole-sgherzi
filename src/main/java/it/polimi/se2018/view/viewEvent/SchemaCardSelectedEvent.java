package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;

public class SchemaCardSelectedEvent extends ViewEvent {

    private final Side side;
    private int schemaCardId;

    public SchemaCardSelectedEvent(String emitter, String player, int schemaCardId, Side side) {
        super(emitter, player);
        this.side = side;
        if (schemaCardId >= 0 && schemaCardId < 2) {
            this.schemaCardId = schemaCardId;
        } else
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": number must be between 0 and 3.");
    }

    public int getSchemaCardId() {
        return schemaCardId;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public State visit(State state) {
        return state.handleSchemaCardSelectedEvent(this);
    }
}
