package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Event;

public interface CLIState {
    public CLIState doAction(CLI cli, Event e);
}
