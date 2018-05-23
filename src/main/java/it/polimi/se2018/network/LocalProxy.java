package it.polimi.se2018.network;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.VirtualView;

abstract public class LocalProxy {
    VirtualView view;
    public abstract void sendEventToClient(Event event);
    public void dispatchEventToVirtualView(Event event){
        view.dispatchProxyEvent(event);
    }

    public VirtualView getView() {
        return view;
    }

    public void setView(VirtualView view) {
        this.view = view;
    }


}
