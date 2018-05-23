package it.polimi.se2018.utils;

public abstract class Event {
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
                ", class='" + getClass().getSimpleName() +'\''+
                '}';
    }
}
