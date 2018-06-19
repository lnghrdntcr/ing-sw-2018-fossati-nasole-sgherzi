package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.GameColor;

import java.awt.*;

public class DoubleMoveOfColorDiceEvent extends DoubleMoveDiceEvent {

    private GameColor color;

    protected DoubleMoveOfColorDiceEvent(String emitter, String receiver, String player, int position, Point source1, Point destination1, Point source2, Point destination2, GameColor color) {
        super(emitter, receiver, player, position, source1, destination1, source2, destination2);
        this.color = color;
    }

    public GameColor getColor() {
        return color;
    }
}
