package it.polimi.se2018.utils;

import it.polimi.se2018.controller.states.State;

import java.io.Serializable;

/**
 * A generic event.
 * Each event should extend this class
 * The player is the receiver of the event. Only this player will receive the event
 */
public abstract class Event implements Serializable {
    private final String player;
    private String emitter;

    protected Event(String emitter, String player) {
        this.emitter = emitter;
        this.player = player;
    }

    public String getEmitterName() {
        return emitter;
    }

    public String getPlayerName() {
        return player;
    }

    @Override
    public String toString() {
        return "Event{" +
                "player='" + player + '\'' +
                ", emitter='" + emitter + '\'' +
                ", class='" + getClass().getSimpleName() + '\'' +
                '}';
    }


}
