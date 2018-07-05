package it.polimi.se2018.view.viewEvent;

import org.json.JSONObject;

public class ChangeDiceNumberEvent extends DiceActionEvent {
    private final int number;

    public ChangeDiceNumberEvent(String emitter, String receiver, String player, int toolCardIndex, int dicePosition, int number) {
        super(emitter, receiver, player, toolCardIndex, dicePosition);
        this.number = number;
    }

    public ChangeDiceNumberEvent(String json){
        super(json);
        this.number=new JSONObject(json).getInt("number");
    }

    public int getNumber() {
        return number;
    }
}
