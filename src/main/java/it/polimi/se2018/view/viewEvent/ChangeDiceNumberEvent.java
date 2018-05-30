package it.polimi.se2018.view.viewEvent;

public class ChangeDiceNumberEvent extends DiceActionEvent {
    private final int number;

    protected ChangeDiceNumberEvent(String emitter, String player, int toolCardIndex, int dicePosition, int number) {
        super(emitter, player, toolCardIndex, dicePosition);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
