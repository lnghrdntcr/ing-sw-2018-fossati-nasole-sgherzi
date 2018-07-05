package it.polimi.se2018.utils;

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

    /**
     * Creates an {@link Event} based on it's JSON representation
     * @param json a valid json string
     */
    public Event(String json) {
        player = new JSONObject(json).getString("playerName");
        emitter = new JSONObject(json).getString("emitterName");
        receiver = new JSONObject(json).getString("receiver");
    }

    public String getEmitterName() {
        return emitter;
    }

    public String getPlayerName() {
        return player;
    }

    /**
     * Removes some elements from an event in order to obscure some informations to an other player
     * @param playername the name of the player that will receive the event
     * @return the same event, but without the sensitive informations
     */
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

    /**
     * Creates a {@link JSONObject} that represent the current {@link Event}
     *
     * @return a {@link JSONObject} that represents the current {@link Event}
     */
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject(this);
        jsonObject.put("class", this.getClass().getCanonicalName());
        return jsonObject;
    }

    /**
     * Recreate an {@link Event} based on a json string
     *
     * @param json a {@link String} that contains a valid JSON representation of an Event
     * @return the event contained in the json
     * @throws ClassNotFoundException if the JSON contains an invalid class (not on the server)
     */
    public static Event decodeJSON(String json) throws ClassNotFoundException {
        try {
            Class<? extends Event> c = (Class<? extends Event>) Class.forName(new JSONObject(json).getString("class"));
            Constructor<? extends Event> constructor = c.getConstructor(String.class);
            Event event = constructor.newInstance(json);

            return event;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | ClassCastException e) {
            e.printStackTrace();
            throw new ClassNotFoundException("Cannot reconstruct class", e);
        }
    }

    public String getReceiver() {
        return receiver;
    }
}
