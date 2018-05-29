package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;

import java.awt.*;

public class PlaceDiceEvent extends Event {

    private Point point;
    private int diceFaceIndex;


    protected PlaceDiceEvent(String emitter, String player, int diceFaceIndex, Point point) {
        super(emitter, player);
        this.point=point;
        this.diceFaceIndex=diceFaceIndex;
    }

    public int getDiceFaceIndex() {
        return diceFaceIndex;
    }

    public Point getPoint() {
        return point;
        //nothing
    }
}
