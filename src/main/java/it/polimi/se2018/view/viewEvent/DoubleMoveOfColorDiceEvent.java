package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.GameColor;
import org.json.JSONObject;

import java.awt.*;

public class DoubleMoveOfColorDiceEvent extends DoubleMoveDiceEvent {

    private GameColor color;

    public DoubleMoveOfColorDiceEvent(String emitter, String receiver, String player, int position, Point source1, Point destination1, Point source2, Point destination2, GameColor color) {
        super(emitter, receiver, player, position, source1, destination1, source2, destination2);
        this.color = color;
    }

    public DoubleMoveOfColorDiceEvent(String json){
        super(json);
        String colorString = new JSONObject(json).getString("color");

        for(GameColor gc : GameColor.values()){
            if(colorString.equalsIgnoreCase(gc.toString())){
                color=gc;
            }
        }
    }

    public GameColor getColor() {
        return color;
    }
}
