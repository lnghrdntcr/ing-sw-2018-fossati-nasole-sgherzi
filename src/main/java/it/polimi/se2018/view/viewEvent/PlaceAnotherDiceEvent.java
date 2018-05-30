package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;

import java.awt.*;

public class PlaceAnotherDiceEvent extends UseToolcardEvent {

    private Point point;
    private int diceFaceIndex;

    protected PlaceAnotherDiceEvent(String emitter, String player, int toolCardIndex, Point point, int diceFaceIndex) {
        super(emitter, player, toolCardIndex);
        this.point = point;
        this.diceFaceIndex = diceFaceIndex;
    }


    public int getDiceFaceIndex() {
        return diceFaceIndex;
    }

    public Point getPoint() {
        return point;
    }
}
