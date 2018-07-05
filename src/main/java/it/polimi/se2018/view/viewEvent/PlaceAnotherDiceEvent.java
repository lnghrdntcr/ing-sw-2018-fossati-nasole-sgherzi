package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Utils;
import org.json.JSONObject;

import java.awt.*;

public class PlaceAnotherDiceEvent extends UseToolcardEvent {

    private Point point;
    private int diceFaceIndex;

    public PlaceAnotherDiceEvent(String emitter,String receiver, String player, int toolCardIndex, Point point, int diceFaceIndex) {
        super(emitter, receiver, player, toolCardIndex);
        this.point = point;
        this.diceFaceIndex = diceFaceIndex;
    }

    public PlaceAnotherDiceEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        diceFaceIndex=jsonObject.getInt("diceFaceIndex");
        point=Utils.decodePosition(jsonObject.getJSONObject("point"));
    }


    public int getDiceFaceIndex() {
        return diceFaceIndex;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();

        jsonObject.put("point", new JSONObject(point));

        return jsonObject;
    }
}
