package it.polimi.se2018.model.event;

public abstract class Event {
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
}
