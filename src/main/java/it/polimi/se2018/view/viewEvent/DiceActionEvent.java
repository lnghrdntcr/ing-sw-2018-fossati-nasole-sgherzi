package it.polimi.se2018.view.viewEvent;

public class DiceActionEvent extends UseToolcardEvent {
    private final int dicePosition;

    protected DiceActionEvent(String emitter, String player, int toolCardIndex, int dicePosition) {
        super(emitter, player, toolCardIndex);
        this.dicePosition = dicePosition;
    }

    public int getDicePosition() {
        return dicePosition;
    }
}
