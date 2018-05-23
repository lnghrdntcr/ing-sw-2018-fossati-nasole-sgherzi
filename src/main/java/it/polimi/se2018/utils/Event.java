package it.polimi.se2018.utils;

import java.io.Serializable;

public abstract class Event implements Serializable {
    private final String player;
    private String emitter;

    protected Event(String emitter, String player) {
        this.emitter = emitter;
        this.player = player;
    }

    public String getEmitter() {
        return emitter;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "Event{" +
                "player='" + player + '\'' +
                ", emitter='" + emitter + '\'' +
                ", class='" + getClass().getSimpleName() +'\''+
                '}';
    }
}
