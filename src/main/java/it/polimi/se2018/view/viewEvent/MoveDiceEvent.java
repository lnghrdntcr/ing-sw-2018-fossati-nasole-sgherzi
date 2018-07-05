package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Utils;
import org.json.JSONObject;

import java.awt.*;

public class MoveDiceEvent extends UseToolcardEvent {
    private final Point source;
    private final Point destination;

    public MoveDiceEvent(String emitter, String receiver, String player, int position, Point source, Point destination) {
        super(emitter, receiver, player, position);
        this.source = source;
        this.destination = destination;
    }

    public MoveDiceEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        source=Utils.decodePosition(jsonObject.getJSONObject("source"));
        destination=Utils.decodePosition(jsonObject.getJSONObject("destination"));

    }

    public Point getSource() {
        return source;
    }

    public Point getDestination() {
        return destination;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();

        jsonObject.put("source", new JSONObject(source));
        jsonObject.put("destination", new JSONObject(destination));

        return jsonObject;
    }

}
