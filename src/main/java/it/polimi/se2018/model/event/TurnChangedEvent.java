package it.polimi.se2018.model.event;

public class TurnChangedEvent extends Event {
    int round;

    public TurnChangedEvent(String emitter, String player, int round) {
        super(emitter, player);
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}
