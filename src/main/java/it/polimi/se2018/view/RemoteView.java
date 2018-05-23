package it.polimi.se2018.view;

import it.polimi.se2018.model.modelEvent.TurnChangedEvent;
import it.polimi.se2018.utils.Event;

public class RemoteView extends View {

    public RemoteView() {
    }

    @Override
    public void update(Event message) {
        System.out.println(message.toString());
    }

    private void moHoSchiacciatoOBottone(){
        notify(new TurnChangedEvent("Bottone", "playyyy", 0));
    }

}
