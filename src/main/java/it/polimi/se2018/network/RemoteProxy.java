package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;

abstract public class RemoteProxy extends Observable<Event> implements Observer<Event> {
    protected void dispatchEventToRemoteView(Event event){
        notify(event);
    }

    abstract void sendEventToServer(Event event);

    @Override
    public void update(Event message) {
        sendEventToServer(message);
    }
}
