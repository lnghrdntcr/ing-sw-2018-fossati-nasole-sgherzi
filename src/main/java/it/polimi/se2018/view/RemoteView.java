package it.polimi.se2018.view;

import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.CLI.CLIGameTable;
import it.polimi.se2018.view.CLI.CLISelectSchema;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteView extends View {

    private ConcurrentLinkedQueue<VisitableFromView> eventLoop = new ConcurrentLinkedQueue<>();
    private Thread eventLoopHandler;
    private SelectSchemaCardFace selectSchemaCardFace;
    private GameEnding gameEnding;
    private GameTable gameTable;

    public RemoteView(String player, Graphics graphics) {
        super(player);
        if (graphics == Graphics.CLI) {
            selectSchemaCardFace = new CLISelectSchema(this);
            gameEnding = null;
            gameTable = new CLIGameTable(this);
        }
        this.startEventLoopHandler();

        activateSelectSchemaCard();

    }

    private void startEventLoopHandler() {
        this.eventLoopHandler = new Thread(() -> {

            while (true) {
                if (!this.eventLoop.isEmpty()) {
                    // TODO: Send event to the CLI/GUI
                    VisitableFromView actualEvent = this.eventLoop.poll();

                    actualEvent.visit(selectSchemaCardFace);
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

        }, "RemoteViewEventLoopHandler");
    }

    @Override
    public void update(Event message) {
        try {
            this.eventLoop.add((VisitableFromView) message);
        } catch (ClassCastException e) {
            Log.d("I couldn't recive this event! " + message);
        }
    }

    public void sendEventToController(ViewEvent event) {
        this.notify(event);
    }

    public void activateSelectSchemaCard(){

        this.gameEnding.setInactive();
        this.gameTable.setInactive();
        this.selectSchemaCardFace.setActive();
    }

    public void activateGameTable() {
        this.selectSchemaCardFace.setInactive();
        this.gameEnding.setInactive();
        this.gameTable.setActive();
    }

    public void activateGameEnding() {
        this.selectSchemaCardFace.setInactive();
        this.gameTable.setInactive();
        this.gameEnding.setActive();
    }

    public enum Graphics {GUI, CLI}
}
