package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.utils.Event;
import org.json.JSONObject;

public abstract class UseToolcardEvent extends ViewEvent {

    private final int toolCardIndex;

    protected UseToolcardEvent(String emitter, String receiver, String player, int toolCardIndex) {
        super(emitter, player, receiver);
        this.toolCardIndex = toolCardIndex;
    }

    protected UseToolcardEvent(String json){
        super(json);
        toolCardIndex=new JSONObject(json).getInt("toolCardIndex");
    }

    public int getToolCardIndex() {
        return toolCardIndex;
    }

    @Override
    public State visit(State state) {
        return state.handleToolcardEvent(this);
    }
}
