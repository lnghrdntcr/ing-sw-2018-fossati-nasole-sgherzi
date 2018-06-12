package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * Event that is used to inform that the DraftBoard has been changed
 */
public class DraftBoardChangedEvent extends ModelEvent {
    DraftBoardImmutable draftBoardImmutable;

    public DraftBoardChangedEvent(String emitter, String player, DraftBoardImmutable draftBoardImmutable) {
        super(emitter, player);
        this.draftBoardImmutable = draftBoardImmutable;
    }

    public DraftBoardImmutable getDraftBoardImmutable() {
        return draftBoardImmutable;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleDraftBoardChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
