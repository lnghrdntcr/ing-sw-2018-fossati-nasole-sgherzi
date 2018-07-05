package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

public class PlayerTimeoutEvent extends ControllerEvent {
    public PlayerTimeoutEvent(String emitter, String player, String receiver) {
        super(emitter, player, receiver);
    }

    public PlayerTimeoutEvent(String json){
        super(json);
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handlePlayerTimeout(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
