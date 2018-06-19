package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Event;

import java.awt.*;

public class MoveDiceEvent extends UseToolcardEvent {
    Point source;
    Point destination;

    public MoveDiceEvent(String emitter, String receiver, String player, int position, Point source, Point destination) {
        super(emitter, receiver, player, position);
        this.source = source;
        this.destination = destination;
    }

    public Point getSource() {
        return source;
    }

    public Point getDestination() {
        return destination;
    }

}
