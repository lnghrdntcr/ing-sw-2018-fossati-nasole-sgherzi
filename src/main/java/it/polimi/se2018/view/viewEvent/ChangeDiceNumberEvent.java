package it.polimi.se2018.view.viewEvent;

public class ChangeDiceNumberEvent extends DiceActionEvent {
    private final int number;

    public ChangeDiceNumberEvent(String emitter, String receiver, String player, int toolCardIndex, int dicePosition, int number) {
        super(emitter, receiver, player, toolCardIndex, dicePosition);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
