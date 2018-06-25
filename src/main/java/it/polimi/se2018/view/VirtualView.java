package it.polimi.se2018.view;

import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.viewEvent.PlayerDisconnectedEvent;
import it.polimi.se2018.view.viewEvent.ViewEvent;

public class VirtualView extends View {
    private LocalProxy localProxy;

    public VirtualView(String player, LocalProxy localProxy) {
        super(player);
        this.connect(localProxy);
    }

    @Override
    public void update(Event message) {
        if(this.isConnected() && localProxy != null) localProxy.sendEventToClient(message.filter(getPlayer()));
    }

    public void dispatchProxyEvent(ViewEvent event) {
        notify(event);
    }

    public void disconnect(){
        this.setConnected(false);
        this.localProxy = null;
        this.dispatchProxyEvent(new PlayerDisconnectedEvent(this.getClass().getName(), this.getPlayer(), ""));
    }

    public void connect(LocalProxy localProxy){

        if(this.localProxy != null) throw new IllegalStateException(this.getClass().getCanonicalName() + " Attempting to connect with an already connected View ");

        this.localProxy = localProxy;
        localProxy.setView(this);

        this.setConnected(true);

    }

}
