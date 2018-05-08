package it.polimi.se2018.model.event;

public abstract class Event {
  private String emitter;

  protected Event(String emitter) {
    this.emitter = emitter;
  }

  public String getEmitter() {
    return emitter;
  }
}
