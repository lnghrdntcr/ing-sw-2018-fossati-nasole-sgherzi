package it.polimi.se2018.view;

import it.polimi.se2018.utils.Event;

public class RemoteView extends View {

    public RemoteView(String player) {
        super(player);
    }

    @Override
    public void update(Event message) {
        // TODO: Send event to the CLI/GUI
        System.out.println(message.toString());
    }

}
