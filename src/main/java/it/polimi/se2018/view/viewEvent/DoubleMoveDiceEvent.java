package it.polimi.se2018.view.viewEvent;

import java.awt.*;

public class DoubleMoveDiceEvent extends UseToolcardEvent{

    private Point source[] = new Point[2];
    private Point destination[] = new Point[2];

    protected DoubleMoveDiceEvent(String emitter,String receiver, String player, int position, Point source1, Point destination1, Point source2, Point destination2) {
        super(emitter, receiver,player, position);
        this.source[1] = source1;
        this.destination[1] = destination1;
        this.source[2] = source2;
        this.destination[2] = destination2;
    }

    public Point getSource(int pos) {
        if(pos<0||pos>2) throw new IllegalArgumentException("Position should be 0 or 1");
        return source[pos];
    }

    public Point getDestination(int pos) {
        if(pos<0||pos>2) throw new IllegalArgumentException("Position should be 0 or 1");
        return destination[pos];
    }
}
