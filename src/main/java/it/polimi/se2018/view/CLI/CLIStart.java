package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.RemoteView;

public class CLIStart extends RemoteView implements CLIState {

    public CLIStart(String player) {
        super(player);
    }

    @Override
    public CLIState doAction(CLI cli, Event e) {
        return new CLISetTools(this.getPlayer());
    }
}
