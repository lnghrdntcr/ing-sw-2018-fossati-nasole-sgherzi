package it.polimi.se2018.view.viewEvent;

import java.awt.*;

public class PlaceAnotherDiceSelectingNumberEvent extends PlaceAnotherDiceEvent {
    private final int number;

    protected PlaceAnotherDiceSelectingNumberEvent(String emitter, String receiver, String player, int toolCardIndex, Point point, int diceFaceIndex, int number) {
        super(emitter, receiver, player, toolCardIndex, point, diceFaceIndex);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
