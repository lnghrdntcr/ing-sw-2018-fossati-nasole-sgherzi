package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.controller.controllerEvent.ControllerEvent;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectScheaCardFace;

public class PlayerTimeoutEvent extends ControllerEvent {
    public PlayerTimeoutEvent(String emitter, String player) {
        super(emitter, player);
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
