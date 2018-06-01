package it.polimi.se2018.controller.controllerEvent;

public class AskPlaceRedrawDiceWithNumberSelectionEvent extends ControllerEvent {
    private final int diceIndex;

    public AskPlaceRedrawDiceWithNumberSelectionEvent(String emitter, String player, int diceIndex) {
        super(emitter, player);
        this.diceIndex = diceIndex;
    }

    public int getDiceIndex() {
        return diceIndex;
    }
}
