package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.utils.Event;

public abstract class UseToolcardEvent extends ViewEvent {

    private int toolCardIndex;

    protected UseToolcardEvent(String emitter, String player, int toolCardIndex) {
        super(emitter, player);
        this.toolCardIndex = toolCardIndex;
    }

    public int getToolCardIndex() {
        return toolCardIndex;
    }

    @Override
    public State visit(State state) {
        return state.handleToolcardEvent(this);
    }
}
