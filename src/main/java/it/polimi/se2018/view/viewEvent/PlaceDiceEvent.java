package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Utils;
import org.json.JSONObject;

import java.awt.*;

public class PlaceDiceEvent extends ViewEvent {

    private Point point;
    private int diceFaceIndex;


    public PlaceDiceEvent(String emitter, String receiver, String player, int diceFaceIndex, Point point) {
        super(emitter, player, receiver);
        this.point = point;
        this.diceFaceIndex = diceFaceIndex;
    }

    public PlaceDiceEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        diceFaceIndex=jsonObject.getInt("diceFaceIndex");
        point = Utils.decodePosition(jsonObject.getJSONObject("point"));

    }

    public int getDiceFaceIndex() {
        return diceFaceIndex;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public State visit(State state) {
        return state.handlePlaceDiceEvent(this);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();

        jsonObject.put("object", new JSONObject(point));

        return jsonObject;
    }
}
