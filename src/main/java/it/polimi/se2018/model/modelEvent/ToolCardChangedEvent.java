package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * Event to inform that a ToolCard property has been changed
 */
public class ToolCardChangedEvent extends ModelEvent {
    private ToolCardImmutable toolCardImmutable;
    private int index;

    public ToolCardChangedEvent(String emitter, String receiver, String player, ToolCardImmutable toolCardImmutable, int index) {
        super(emitter, player, receiver);
        this.toolCardImmutable = toolCardImmutable;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public ToolCardImmutable getToolCardImmutable() {
        return toolCardImmutable;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleToolcardChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
