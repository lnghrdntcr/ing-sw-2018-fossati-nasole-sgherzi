package it.polimi.se2018.controller;

import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
import it.polimi.se2018.controller.controllerEvent.TimeoutCommunicationEvent;
import it.polimi.se2018.controller.controllerEvent.ViewPlayerTimeoutEvent;
import it.polimi.se2018.controller.states.GameEndState;
import it.polimi.se2018.controller.states.GameSetupState;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.objectives.*;
import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.utils.*;
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
    private boolean gameStarted = false;
    private ConcurrentLinkedQueue<Event> outboundEventLoop = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<ViewEvent> inboundEventLoop = new ConcurrentLinkedQueue<>();

    private boolean isGameEnded = false;

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


    /**
     * Starts the timeout, created the fist state, starts the event loop.
     */
    public void start() {

        if (state != null) throw new IllegalStateException("Game already started!");

        Log.d("Starting timeout...");

        state = new GameSetupState(this, model);

        this.startEventLoopHandlerThread();
        this.startActionTimeout();

        model.onGameStart();

    }

    /**
     * Gets the timeout.
     *
     * @return the remaining seconds for the player to take an action.
     */
    public int getTimeout() {
        return (int) ((System.currentTimeMillis() - this.beginTime) / 1000L);
    }

    /**
     * Creates all names of the toolcards, shuffles them and then chooses 3 random toolcards.
     *
     * @return Settings.TOOLCARDS_N toolcard names.
     */
    private String[] pickToolCards() {

        ArrayList<String> tools = new ArrayList<>();
        /*tools.add("CircularCutter"); // TESTED on GUI
        tools.add("CopperReamer"); // TESTED on gui
        tools.add("CorkRow"); // TESTED On GUI*/
/*        tools.add("DiamondPad"); // tested on gui
        tools.add("EglomiseBrush"); // Tested on gui
        tools.add("FirmPastaDiluent"); // Tested on gui*/
/*        tools.add("FirmPastaBrush"); // Tested on gui
        tools.add("Gavel"); // Strange behaviour
        tools.add("Lathekin"); // Tested on gui*/
        tools.add("ManualCutter");
        tools.add("RoughingNipper");
        tools.add("WheeledPincer"); // Tested on gui

        Collections.shuffle(tools);
        return tools.subList(0, Settings.TOOLCARDS_N).toArray(new String[Settings.TOOLCARDS_N]);
    }

    /**
     * Creates all of the PublicObjectives, shuffles it and then picks 3 random public objectives.
     *
     * @return An array of public objectives.
     */
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
        if (message.getPlayerName().equals(getModel().getCurrentPlayerName()))
            this.beginTime = System.currentTimeMillis();
        this.inboundEventLoop.add(message);

    }

    /**
     * Starts the timeout for the player action.
     */
    private void startActionTimeout() {

        this.actionTimeoutThread = new Thread(() -> {

            while (true) {
                this.beginTime = System.currentTimeMillis();
                while ((System.currentTimeMillis() - this.beginTime) < this.actionTimeout) {

                    this.outboundEventLoop.add(new TimeoutCommunicationEvent(this.getClass().getName(), "", this.model.getCurrentPlayerName(), (int) (this.actionTimeout / 1000L - this.getTimeout())));

                    try {
                        Thread.sleep(1000);
                        Log.i("[ACTION_TIMEOUT_THREAD] Remaining " + (this.actionTimeout / 1000L - this.getTimeout()) + " seconds");
                    } catch (InterruptedException e) {
                        Log.d("ActionTimeoutThread was interrupted, well... This may be a problem.");
                        e.printStackTrace();
                    }
                }

                this.outboundEventLoop.add(new PlayerTimeoutEvent(this.getClass().getName(), "", this.model.getCurrentPlayerName()));
                this.inboundEventLoop.add(new ViewPlayerTimeoutEvent(this.getClass().getName(), "", this.getModel().getCurrentPlayerName()));

            }
        }, "ActionTimeoutThread");

        this.actionTimeoutThread.start();

    }

    /**
     * Starts the event loop thread.
     */
    private void startEventLoopHandlerThread() {

        this.eventLoopHandlerThread = new Thread(() -> {

            while (true) {

                while (!this.outboundEventLoop.isEmpty()) {

                    this.notify(this.outboundEventLoop.poll());

                }

                while (!this.inboundEventLoop.isEmpty()) {

                    ViewEvent inboundEvent = this.inboundEventLoop.poll();

                    this.state = state.handleEvent(inboundEvent, this.model);

                    if (!getModel().hasNextTurn() && !isGameEnded) {
                        isGameEnded = true;
                        this.state = new GameEndState(this, getModel());
                    }

                    Log.d(getClass().getName() + " Going in new state: " + state + " event: " + inboundEvent);

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

    public boolean isMoreThanOnePlayerConnected() {
        int i = 0;
        for (View v : this.views) {
            if (v.isConnected()) i++;
        }

        return i > 1;
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

    /**
     * Reconnects the player.
     *
     * @param localProxy The localProxy of the reconnected player.
     * @param playerName The name of the player to reconnect.
     */
    public void reconnectPlayer(LocalProxy localProxy, String playerName) {

        for (View v : this.views) {
            if (v.getPlayer().equals(playerName) && !v.isConnected()) {
                ((VirtualView) v).connect(localProxy);
                getModel().sync(playerName);
                Log.d("Player " + playerName + " reconnected successfully ");
            }
        }

    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted() {
        this.gameStarted = true;
    }
}
