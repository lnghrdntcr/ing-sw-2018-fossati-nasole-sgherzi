package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Event;

import java.awt.*;

public class CopperReamerEvent extends Event {
    Point source;
    Point destination;
    DiceFace diceFace;

    protected CopperReamerEvent(String emitter, String player, DiceFace diceFace, Point source, Point destination) {
        super(emitter, player);
        this.diceFace = diceFace;
        this.source = source;
        this.destination = destination;
    }

    public Point getSource() {
        return source;
    }

    public Point getDestination() {
        return destination;
    }

    public DiceFace getDiceFace() {
        return diceFace;
    }
}
