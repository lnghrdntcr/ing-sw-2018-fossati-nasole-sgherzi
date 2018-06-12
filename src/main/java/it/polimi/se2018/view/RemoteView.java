package it.polimi.se2018.view;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteView extends View {

    private ConcurrentLinkedQueue<VisitableFromView> eventLoop = new ConcurrentLinkedQueue<>();
    private Thread eventLoopHandler;
    private SelectScheaCardFace selectScheaCardFace;
    private GameEnding gameEnding;
    private GameTable gameTable;

    public RemoteView(String player) {
        super(player);
        // TODO: Instantiate SelectScheaCardFace, GameEnding, GameTable.
        this.startEventLoopHandler();

    }

    private void startEventLoopHandler() {
        this.eventLoopHandler = new Thread(() -> {

            while (true){
                if(!this.eventLoop.isEmpty()){
                    // TODO: Send event to the CLI/GUI
                    VisitableFromView actualEvent = this.eventLoop.poll();

                    actualEvent.visit(selectScheaCardFace);
                    actualEvent.visit(gameTable);
                    actualEvent.visit(gameEnding);

                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Log.d("eventLoopHandlerThread was interrupted");
                    // Should I restart it?
                    // this.eventLoopHandlerThread.start();
                }

            }

        },"RemoteViewEventLoopHandler");
    }

    @Override
    public void update(Event message) {
        try{
            this.eventLoop.add((VisitableFromView) message);
        } catch (ClassCastException e ){
            Log.d("I couldn't recive this event! " + message);
        }
    }

    void sendEventToController(ViewEvent event){
        this.notify(event);
    }

}
