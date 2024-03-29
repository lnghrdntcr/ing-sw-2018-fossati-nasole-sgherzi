package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
import it.polimi.se2018.view.viewEvent.ViewEvent;

/**
 * A class that represents the server on the client side.
 * It dispatchs events from the {@link it.polimi.se2018.view.RemoteView} to the server and vice versa
 */
abstract public class RemoteProxy extends Observable<Event> implements Observer<ViewEvent> {
    /**
     * Dispatch an {@link Event} received from the server to the connected {@link it.polimi.se2018.view.RemoteView}
     * @param event
     */
    protected void dispatchEventToRemoteView(Event event){
        notify(event);
    }

    /**
     * Sends the specified event to the connected server
     * @param event the event to send
     */
    abstract void sendEventToServer(ViewEvent event);

    //This is the observer's method
    @Override
    public void update(ViewEvent message) {
        sendEventToServer(message);
    }
}
