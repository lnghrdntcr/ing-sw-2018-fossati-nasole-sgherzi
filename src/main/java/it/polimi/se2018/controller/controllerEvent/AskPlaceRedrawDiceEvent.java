package it.polimi.se2018.controller.controllerEvent;

public class AskPlaceRedrawDiceEvent extends ControllerEvent {
    private final int diceIndex;

    public AskPlaceRedrawDiceEvent(String emitter, String player, int diceIndex) {
        super(emitter, player);
        this.diceIndex = diceIndex;
    }

    public int getDiceIndex() {
        return diceIndex;
    }
}
