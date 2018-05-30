package it.polimi.se2018.controller.controllerEvent;

import java.awt.*;

public class CopperReamerEvent extends ControllerEvent {
    Point source;
    Point destination;


    protected CopperReamerEvent(String emitter, String player, Point source, Point destination) {
        super(emitter, player);
        this.source =source;
        this.destination=destination;
    }

    public Point getSource() {
        return source;
    }

    public Point getDestination() {
        return destination;
    }


}
