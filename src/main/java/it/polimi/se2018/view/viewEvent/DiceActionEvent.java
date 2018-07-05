package it.polimi.se2018.view.viewEvent;

import org.json.JSONObject;

public class DiceActionEvent extends UseToolcardEvent {
    private final int dicePosition;

    public DiceActionEvent(String emitter, String receiver, String player, int toolCardIndex, int dicePosition) {
        super(emitter, receiver, player, toolCardIndex);
        this.dicePosition = dicePosition;
    }

    public DiceActionEvent(String json){
        super((json));
        dicePosition=new JSONObject(json).getInt("dicePosition");
    }

    public int getDicePosition() {
        return dicePosition;
    }
}
