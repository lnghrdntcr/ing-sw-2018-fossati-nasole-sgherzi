package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import it.polimi.se2018.view.VisitableFromView;

public class GameStartEvent extends ControllerEvent implements VisitableFromView {
    public GameStartEvent(String emitter, String player, String receiver) {
        super(emitter, player, receiver);
    }

    @Override
    public void visit(GameTable gameTable) {
        Log.d("Expected to call handleGameStart on " + gameTable.getClass().getName());
        Log.d(gameTable.getClass().getName());
        gameTable.handleGameStart(this);
    }

    public GameStartEvent(String json){
        super(json);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }
}
