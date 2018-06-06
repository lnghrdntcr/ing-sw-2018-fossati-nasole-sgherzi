package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.modelEvent.DraftBoardChangedEvent;
import it.polimi.se2018.utils.Event;

public class CLISetDraft implements CLIState {
    @Override
    public CLIState doAction(CLI cli, Event e) {
        cli.setDraftBoard(((DraftBoardChangedEvent) e).getDraftBoardImmutable());
        return new CLISetDiceHolder();
    }
}
