package it.polimi.se2018.view;

import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.viewEvent.PlayerDisconnectedEvent;
import it.polimi.se2018.view.viewEvent.ViewEvent;

/**
 * A View that lives on the server side. It is connected with the model and the controller and, through the network, sends and receive Event to a RemoteView that lives on the client side.
 */
public class VirtualView extends View {
    private LocalProxy localProxy;

    public VirtualView(String player, LocalProxy localProxy) {
        super(player);
        this.connect(localProxy);
    }

    /**
     * Dispatches the Event to the remotely connected view
     * @param message a message passed from the {@link it.polimi.se2018.utils.Observable}
     */
    @Override
    public void update(Event message) {
        if(this.isConnected() && localProxy != null) localProxy.sendEventToClient(message.filter(getPlayer()));
    }

    /**
     * Dispatch an event coming from the proxy used for the connection
     * @param event the event to dispatch
     */
    public void dispatchProxyEvent(ViewEvent event) {
        notify(event);
    }

    /**
     * Disconenct the view from the current proxy
     */
    public void disconnect(){
        this.setConnected(false);
        this.localProxy = null;
        this.dispatchProxyEvent(new PlayerDisconnectedEvent(this.getClass().getName(), this.getPlayer(), ""));
    }

    /**
     * Connects the view with the specified proxy
     * @param localProxy the proxy to use for the connection
     */
    public void connect(LocalProxy localProxy){

        if(this.localProxy != null) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Attempting to connect with an already connected View ");

        this.localProxy = localProxy;

        localProxy.setView(this);

        this.setConnected(true);

    }

}
