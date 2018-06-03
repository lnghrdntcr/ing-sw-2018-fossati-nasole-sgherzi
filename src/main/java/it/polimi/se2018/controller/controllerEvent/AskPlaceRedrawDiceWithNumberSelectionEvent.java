package it.polimi.se2018.controller.controllerEvent;

/**
 * Event that is used to ask player to place a redrawn dice, after the selection of the number
 */
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
