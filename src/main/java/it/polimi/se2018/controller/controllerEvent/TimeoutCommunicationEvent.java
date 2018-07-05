package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

public class TimeoutCommunicationEvent extends ControllerEvent {

    private final int timeout;

    public TimeoutCommunicationEvent(String emitter, String receiver, String player, int timeout) {
        super(emitter, player, receiver);
        this.timeout = timeout;
    }

    public TimeoutCommunicationEvent(String json){
        super(json);
        this.timeout=new JSONObject(json).getInt("timeout");
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleTimeoutCommunication(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {
        selectSchemaCardFace.handleTimeoutCommunication(this);
    }

    public int getTimeout() {
        return timeout;
    }
}
