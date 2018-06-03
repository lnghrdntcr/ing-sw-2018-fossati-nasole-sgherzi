package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;

/**
 * Event to inform that a ToolCard property has been changed
 */
public class ToolCardChanged extends ModelEvent {
    ToolCardImmutable toolCardImmutable;

    public ToolCardChanged(String emitter, String player, ToolCardImmutable toolCardImmutable) {
        super(emitter, player);
        this.toolCardImmutable = toolCardImmutable;
    }

    public ToolCardImmutable getToolCardImmutable() {
        return toolCardImmutable;
    }
}
