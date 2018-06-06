package it.polimi.se2018.controller.controllerEvent;

public class TimeoutCommunicationEvent extends ControllerEvent {

    private int timeout;

    public TimeoutCommunicationEvent(String emitter, String player, int timeout) {
        super(emitter, player);
        this.timeout = timeout;
    }
}
