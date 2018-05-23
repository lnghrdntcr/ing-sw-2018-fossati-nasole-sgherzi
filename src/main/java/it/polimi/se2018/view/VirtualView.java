package it.polimi.se2018.view;

import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.utils.Event;

public class VirtualView extends View {
    private LocalProxy localProxy;

    public VirtualView(LocalProxy localProxy) {
        this.localProxy = localProxy;
        localProxy.setView(this);
    }

    @Override
    public void update(Event message) {
        localProxy.sendEventToClient(message);
    }

    public void dispatchProxyEvent(Event event){
        notify(event);
    }


}
