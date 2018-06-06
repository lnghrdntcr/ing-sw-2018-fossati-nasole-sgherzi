package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.modelEvent.ToolCardChanged;
import it.polimi.se2018.utils.Event;

public class CLISetTools implements CLIState {

    private static int c = 0;

    public CLISetTools() {
    }

    @Override
    public CLIState doAction(CLI cli, Event e) {
        cli.getTool().add(((ToolCardChanged) e).getToolCardImmutable());
        c++;

        if (this.c < 2) return new CLISetTools();

        return new CLISetDraft();
    }
}
