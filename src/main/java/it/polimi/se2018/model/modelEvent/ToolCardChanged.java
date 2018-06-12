package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectScheaCardFace;

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

    @Override
    public void visit(GameTable gameTable) {

    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectScheaCardFace selectScheaCardFace) {

    }
}
