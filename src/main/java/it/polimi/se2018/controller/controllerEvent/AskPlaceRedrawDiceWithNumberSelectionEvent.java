package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectScheaCardFace;

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

    @Override
    public void visit(GameTable gameTable) {

    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectScheaCardFace selectScheaCardFace) {

    }
}
