package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.controller.controllerEvent.ControllerEvent;

public class PlayerTimeoutEvent extends ControllerEvent {
    public PlayerTimeoutEvent(String emitter, String player) {
        super(emitter, player);
    }
}