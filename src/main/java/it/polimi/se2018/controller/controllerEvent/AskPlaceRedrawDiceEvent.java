package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * Event that is used to ask player to place a redrawn dice
 */
public class AskPlaceRedrawDiceEvent extends ControllerEvent {
    private final int diceIndex;

    public AskPlaceRedrawDiceEvent(String emitter, String player, int diceIndex) {
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
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
