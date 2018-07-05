package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

/**
 * Event that is used to ask player to place a redrawn dice, after the selection of the number
 */
public class AskPlaceRedrawDiceWithNumberSelectionEvent extends ControllerEvent {
    private final int diceIndex;

    public AskPlaceRedrawDiceWithNumberSelectionEvent(String emitter, String receiver, String player, int diceIndex) {
        super(emitter, player, receiver);
        this.diceIndex = diceIndex;
    }

    public AskPlaceRedrawDiceWithNumberSelectionEvent(String json){
        super(json);
        diceIndex=new JSONObject(json).getInt("diceIndex");
    }

    public int getDiceIndex() {
        return diceIndex;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleAskPlaceRedrawDiceWithNumberSelection(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
