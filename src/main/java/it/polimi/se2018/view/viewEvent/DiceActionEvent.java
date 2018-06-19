package it.polimi.se2018.view.viewEvent;

public class DiceActionEvent extends UseToolcardEvent {
    private final int dicePosition;

    public DiceActionEvent(String emitter, String receiver, String player, int toolCardIndex, int dicePosition) {
        super(emitter, receiver, player, toolCardIndex);
        this.dicePosition = dicePosition;
    }

    public int getDicePosition() {
        return dicePosition;
    }
}
