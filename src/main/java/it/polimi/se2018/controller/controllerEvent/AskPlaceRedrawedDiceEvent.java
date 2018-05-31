package it.polimi.se2018.controller.controllerEvent;

public class AskPlaceRedrawedDiceEvent extends ControllerEvent {
    private final int diceIndex;

    public AskPlaceRedrawedDiceEvent(String emitter, String player, int diceIndex) {
        super(emitter, player);
        this.diceIndex = diceIndex;
    }

    public int getDiceIndex() {
        return diceIndex;
    }
}
