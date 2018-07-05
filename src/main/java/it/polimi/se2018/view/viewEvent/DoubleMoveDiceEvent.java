package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

public class DoubleMoveDiceEvent extends UseToolcardEvent {

    private final Point source[] = new Point[2];
    private final Point destination[] = new Point[2];

    public DoubleMoveDiceEvent(String emitter, String receiver, String player, int position, Point source1, Point destination1, Point source2, Point destination2) {
        super(emitter, receiver, player, position);
        this.source[0] = source1;
        this.destination[0] = destination1;
        this.source[1] = source2;
        this.destination[1] = destination2;
    }

    public DoubleMoveDiceEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONArray sourceJson = jsonObject.getJSONArray("source");
        JSONArray destinationJson = jsonObject.getJSONArray("destination");

        source[0] = Utils.decodePosition(sourceJson.getJSONObject(0));
        source[1] = Utils.decodePosition(sourceJson.getJSONObject(1));

        destination[0] = Utils.decodePosition(destinationJson.getJSONObject(0));
        destination[1] = Utils.decodePosition(destinationJson.getJSONObject(1));
    }

    public Point getSource(int pos) {
        if (pos < 0 || pos > 2) throw new IllegalArgumentException("Position should be 0 or 1");
        return source[pos];
    }

    public Point getDestination(int pos) {
        if (pos < 0 || pos > 2) throw new IllegalArgumentException("Position should be 0 or 1");
        return destination[pos];
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();
        JSONArray source = new JSONArray();
        JSONArray destination = new JSONArray();

        source.put(new JSONObject(getSource(0)));
        source.put(new JSONObject(getSource(1)));

        destination.put(new JSONObject(getDestination(0)));
        destination.put(new JSONObject(getDestination(1)));

        jsonObject.put("source", source);
        jsonObject.put("destination", destination);
        return jsonObject;
    }
}
