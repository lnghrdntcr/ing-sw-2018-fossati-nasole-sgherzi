package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

public class LogEvent extends ControllerEvent {

    private final String message;
    private long timestamp;

    public LogEvent(String emitter, String player, String receiver, String message) {
        super(emitter, player, receiver);
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleLogEvent(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {
    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {
        selectSchemaCardFace.handleLogEvent(this);
    }

    public String getMessage() {
        return message;
    }

}
