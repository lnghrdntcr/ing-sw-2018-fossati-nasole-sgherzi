package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.TimeoutCommunicationEvent;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.CLI.CLIGameEnding;
import it.polimi.se2018.view.CLI.CLIGameTable;
import it.polimi.se2018.view.CLI.CLISelectSchema;
import it.polimi.se2018.view.gui.GUIGameTable;
import it.polimi.se2018.view.gui.GUIGameEnding;
import it.polimi.se2018.view.gui.GUILogger;
import it.polimi.se2018.view.gui.GUISelectSchemaCardFace;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteView extends View {

    private final Graphics graphics;
    private ConcurrentLinkedQueue<VisitableFromView> eventLoop = new ConcurrentLinkedQueue<>();
    private Thread eventLoopHandler;
    private SelectSchemaCardFace selectSchemaCardFace;
    private GameEnding gameEnding;
    private GameTable gameTable;

    public RemoteView(String player, Graphics graphics) {
        super(player);
        this.graphics = graphics;
        if (graphics == Graphics.CLI) {
            selectSchemaCardFace = new CLISelectSchema(this);
            gameEnding = new CLIGameEnding(this);
            gameTable = new CLIGameTable(this);
        } else {
            GUILogger.go();
            selectSchemaCardFace = new GUISelectSchemaCardFace(this);
            gameEnding = new GUIGameEnding(this);
            gameTable = new GUIGameTable(this);
        }


    }

    public void start() {
        this.startEventLoopHandler();
        activateSelectSchemaCard();
    }

    private void startEventLoopHandler() {
        this.eventLoopHandler = new Thread(() -> {

            while (true) {
                if (!this.eventLoop.isEmpty()) {

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
        eventLoopHandler.start();
    }

    @Override
    public void update(Event message) {

        if (!(message instanceof TimeoutCommunicationEvent))
            Log.d(message.toString());


        if (
            message.getReceiver().equals(getPlayer()) || // Message is for me
                message.getReceiver().equals("")             // Message is for everyone v2
            ) {
            try {
                this.eventLoop.add((VisitableFromView) message);
            } catch (ClassCastException e) {
                Log.d("I couldn't receive this event! " + message);
            }
        }


    }

    public void sendEventToController(ViewEvent event) {
        this.notify(event);
    }

    public void activateSelectSchemaCard() {

        this.gameEnding.setInactive();
        this.gameTable.setInactive();
        this.selectSchemaCardFace.setActive();
    }

    public void activateGameTable() {
        this.selectSchemaCardFace.setInactive();
        this.gameTable.setActive();
        this.gameEnding.setInactive();
    }

    public void activateGameEnding() {
        this.selectSchemaCardFace.setInactive();
        this.gameTable.setInactive();
        this.gameEnding.setActive();
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public enum Graphics {GUI, CLI}
}
