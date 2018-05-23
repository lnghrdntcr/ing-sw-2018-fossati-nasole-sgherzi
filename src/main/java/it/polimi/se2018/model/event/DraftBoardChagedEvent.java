package it.polimi.se2018.model.event;

import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.utils.Event;

public class DraftBoardChagedEvent extends Event {
    DraftBoardImmutable draftBoardImmutable;

    public DraftBoardChagedEvent(String emitter, String player, DraftBoardImmutable draftBoardImmutable) {
        super(emitter, player);
        this.draftBoardImmutable = draftBoardImmutable;
    }

    public DraftBoardImmutable getDraftBoardImmutable() {
        return draftBoardImmutable;
    }
}
