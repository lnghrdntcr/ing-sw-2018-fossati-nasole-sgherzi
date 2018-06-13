package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.VirtualView;
import it.polimi.se2018.view.viewEvent.ViewEvent;

/**
 * A class that represents the remote client on the server side.
 * It exposes the correct methods to send and receive event to the client
 */
abstract public class LocalProxy {
    private VirtualView view;

    /**
     * Sends the specified event to the client
     * @param event the event that should me dispatched
     */
    public abstract void sendEventToClient(Event event);

    /**
     * Dipatch a previously received event to the connected {@link VirtualView}
     * @param event the event to dispatch
     */
    public void dispatchEventToVirtualView(ViewEvent event){
        if(event.getPlayerName().equals(view.getPlayer())) {
            view.dispatchProxyEvent(event);
        }else {
            Log.w("Listener for player "+view.getPlayer()+" received an event for "+event.getPlayerName()+", so strange, this motherfather il trying to hack the game...");
        }
    }

    public VirtualView getView() {
        return view;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }


}
