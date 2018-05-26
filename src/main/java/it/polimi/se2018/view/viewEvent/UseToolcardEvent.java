package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;

public class UseToolcardEvent extends Event {

    private int toolCardIndex;

    protected UseToolcardEvent(String emitter, String player, int toolCardIndex) {
        super(emitter, player);
        this.toolCardIndex = toolCardIndex;
    }

    public int getToolCardIndex() {
        return toolCardIndex;
    }
}
