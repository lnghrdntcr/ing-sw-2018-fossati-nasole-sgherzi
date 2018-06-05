package it.polimi.se2018.view;

import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.viewEvent.ViewEvent;

public class VirtualView extends View {
    private LocalProxy localProxy;

    public VirtualView(String player, LocalProxy localProxy) {
        super(player);
        this.localProxy = localProxy;
        localProxy.setView(this);
    }

    @Override
    public void update(Event message) {
        localProxy.sendEventToClient(message);
    }

    public void dispatchProxyEvent(ViewEvent event) {
        notify(event);
    }


}
