package it.polimi.se2018.utils;

import it.polimi.se2018.controller.states.State;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A generic event.
 * Each event should extend this class
 * The player is the receiver of the event. Only this player will receive the event
 */
public abstract class Event implements Serializable {
    private final String player;
    private final String emitter;
    private final String receiver;

    protected Event(String emitter, String player, String receiver) {
        this.emitter = emitter;
        this.player = player;
        this.receiver = receiver;
    }

    public Event(String json){
        player=new JSONObject(json).getString("playerName");
        emitter=new JSONObject(json).getString("emitterName");
        receiver=new JSONObject(json).getString("receiver");
    }

    public String getEmitterName() {
        return emitter;
    }

    public String getPlayerName() {
        return player;
    }

    public Event filter(String playername) {
        return this;
    }

    @Override
    public String toString() {
        return "Event{" +
                "player='" + player + '\'' +
                ", emitter='" + emitter + '\'' +
                ", receiver='" + receiver + '\'' +
                ", class='" + getClass().getSimpleName() + '\'' +
                '}';
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject(this);
        jsonObject.put("class", this.getClass().getCanonicalName());
        return jsonObject;
    }

    public static Event decodeJSON(String json) throws ClassNotFoundException, ClassCastException {
        Class<? extends Event> c = (Class<? extends Event>) Class.forName(new JSONObject(json).getString("class"));
        try {
            Constructor<? extends Event> constructor = c.getConstructor(String.class);
            Event event = constructor.newInstance(json);

            return event;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ClassNotFoundException("Cannot reconstruct class", e);
        }
    }

    public String getReceiver() {
        return receiver;
    }
}
