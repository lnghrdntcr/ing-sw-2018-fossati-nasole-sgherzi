package it.polimi.se2018.controller;

import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
import it.polimi.se2018.controller.controllerEvent.TimeoutCommunicationEvent;
import it.polimi.se2018.controller.states.GameSetupState;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.objectives.*;
import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.network.LocalProxySocket;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.VirtualView;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The main Controller class that handles the communication between the Model and the Views
 */
public class Controller extends Observable<Event> implements Observer<ViewEvent> {

    private GameTableMultiplayer model;
    private State state;
    private List<View> views;
    private ConcurrentLinkedQueue<Event> outboundEventLoop = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<ViewEvent> inboundEventLoop = new ConcurrentLinkedQueue<>();

    private long actionTimeout;
    private long beginTime;
    private Thread actionTimeoutThread;
    private Thread timeoutCommunicationThread;
    private Thread eventLoopHandlerThread;

    public Controller(ArrayList<View> viewArrayList, long actionTimeout) {

        this.actionTimeout = actionTimeout;
        this.views = viewArrayList;

        ArrayList<String> pln = new ArrayList<>();
        //register the Controller as an observer
        //and the view as an observer of the controller
        viewArrayList.forEach((view) -> {
            view.register(this);
            this.register(view);
            pln.add(view.getPlayer());
        });

        model = new GameTableMultiplayer(pickPublicObjectives(), pln.toArray(new String[viewArrayList.size()]), pickToolCards());

        viewArrayList.forEach(view -> model.register(view));

        Log.i("Game created with " + pln.size() + " players");

    }


    public void start() {

        if (state != null) throw new IllegalStateException("Game already started!");

        Log.d("Starting timeout...");

        state = new GameSetupState(this, model);

/*        this.startTimeoutCommunicationThread();*/
        this.startEventLoopHandlerThread();
        this.startActionTimeout();

    }

    public int getTimeout() {
        return (int) ((System.currentTimeMillis() - this.beginTime) / 1000L);
    }

    private String[] pickToolCards() {
        ArrayList<String> tools = new ArrayList<>();
        tools.add("CircularCutter");
        tools.add("CopperReamer");
        tools.add("CorkRow");
        tools.add("DiamondPad");
        tools.add("EglomiseBrush");
        tools.add("FirmPastaDiluent");
        tools.add("FirmPastaBrush");
        tools.add("Gavel");
        tools.add("Lathekin");
        tools.add("ManualCutter");
        tools.add("RoughingNipper");
        tools.add("WheeledPincer");

        Collections.shuffle(tools);
        return tools.subList(0, Settings.TOOLCARDS_N).toArray(new String[Settings.TOOLCARDS_N]);
    }

    private PublicObjective[] pickPublicObjectives() {
        ArrayList<PublicObjective> publicObjectives = new ArrayList<>();
        publicObjectives.add(new ColoredDiagonals());
        publicObjectives.add(new ColorVariety());
        publicObjectives.add(new DarkShades());
        publicObjectives.add(new DifferentColumnColor());
        publicObjectives.add(new DifferentColumnShades());
        publicObjectives.add(new DifferentRowColor());
        publicObjectives.add(new DifferentRowShades());
        publicObjectives.add(new LightShades());
        publicObjectives.add(new MediumShades());

        Collections.shuffle(publicObjectives);
        return publicObjectives.subList(0, Settings.POBJECTIVES_N).toArray(new PublicObjective[Settings.POBJECTIVES_N]);
    }

    @Override
    public void update(ViewEvent message) {
        // This
        // this.resetActionTimeout();
        // or
        // that?
        this.beginTime = System.currentTimeMillis();
        this.inboundEventLoop.add(message);

    }

    private void startActionTimeout() {

        this.actionTimeoutThread = new Thread(() -> {

            while (true) {
                this.beginTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - this.beginTime) < this.actionTimeout) {

                    this.outboundEventLoop.add(new TimeoutCommunicationEvent(this.getClass().getName(), this.model.getCurrentPlayerName(), this.getTimeout()));

                    try {
                        Thread.sleep(1000);
                        Log.i("[ACTION_TIMEOUT_THREAD] Remaining " + (this.actionTimeout / 1000L - this.getTimeout()) + " seconds");
                    } catch (InterruptedException e) {
                        Log.d("ActionTimeoutThread was interrupted, well... This may be a problem.");
                        e.printStackTrace();
                    }
                }

                this.outboundEventLoop.add(new PlayerTimeoutEvent(this.getClass().getName(), this.model.getCurrentPlayerName()));
                this.state = this.state.handleUserTimeOutEvent();
                Log.d(getClass().getName()+" Going in new state: "+state);
            }
        }, "ActionTimeoutThread");

        this.actionTimeoutThread.start();

    }


    /*private void startTimeoutCommunicationThread() {
        this.timeoutCommunicationThread = new Thread(() -> {
            while (true) {

                // Adds a TimeoutCommunicationEvent to the event loop.
                this.outboundEventLoop.add(new TimeoutCommunicationEvent(this.getClass().getName(), this.model.getCurrentPlayerName(), this.getTimeout()));

                // Then waits for 1000ms
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Log.d("TimeoutCommunication Thread was interrupted");
                    // Should I restart it?
                    //this.timeoutCommunicationThread.start();
                }

            }
        }, "TimeoutCommunicationThread");

        this.timeoutCommunicationThread.start();
    }
*/
    private void startEventLoopHandlerThread() {

        this.eventLoopHandlerThread = new Thread(() -> {

            while (true) {

                if (!this.outboundEventLoop.isEmpty()) {
                    for (Event ev : this.outboundEventLoop) {
                        this.notify(this.outboundEventLoop.poll());
                    }
                }

                if (!this.inboundEventLoop.isEmpty()) {
                    for (ViewEvent ev : this.inboundEventLoop) {
                        this.state = state.handleEvent(this.inboundEventLoop.poll(), this.model);
                        Log.d(getClass().getName()+" Going in new state: "+state);
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                    Log.d("eventLoopHandlerThread was interrupted");
                }

            }
        }, "EventLoopHandler");

        this.eventLoopHandlerThread.start();

    }

    /**
     * Checks if a player was connected and then disconnected.
     *
     * @param playerName The player to check the connection to.
     * @return true if the player connected before, false otherwise.
     */
    public boolean isPlayerDisconnected(String playerName) {
        for (View v : this.views) {
            if (playerName.equals(v.getPlayer()) && !v.isConnected()) return true;
        }
        return false;
    }

    public GameTableMultiplayer getModel() {
        return model;
    }

    public String[] getPlayersList() {
        return model.getPlayersName();
    }

    public void dispatchEvent(Event toDispatchEvent) {
        this.outboundEventLoop.add(toDispatchEvent);
    }

    public void reconnectPlayer(LocalProxy localProxy, String playerName) {

        for (View v : this.views) {
            if(v.getPlayer().equals(playerName) && !v.isConnected()){
                ((VirtualView) v).connect(localProxy);
                Log.d("Player " + playerName + " reconnected successfully ");
            }
        }

    }
}
