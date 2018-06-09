package it.polimi.se2018.controller;

import it.polimi.se2018.controller.controllerEvent.TimeoutCommunicationEvent;
import it.polimi.se2018.controller.states.GameSetupState;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.tool.*;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.objectives.*;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.ViewEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The main Controller class that handles the communication between the Model and the Views
 */
public class Controller extends Observable<Event> implements Observer<ViewEvent> {

    private GameTableMultiplayer model;
    private State state;
    private ConcurrentLinkedQueue<Event> outboundEventLoop = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<ViewEvent> inboundEventLoop = new ConcurrentLinkedQueue<>();

    private long actionTimeout;
    private long beginTime;
    private Thread actionTimeoutThread;
    private Thread timeoutCommunicationThread;
    private Thread eventLoopHandlerThread;

    public Controller(ArrayList<View> viewArrayList, long actionTimeout) {

        this.actionTimeout = actionTimeout;

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

        Log.i("Game started with " + pln.size() + " players");

        this.start();
        this.startTimeoutCommunicationThread();
        this.startEventLoopHandlerThread();

    }


    public void start() {

        if (state != null) throw new IllegalStateException("Game already started!");

        Log.d("Starting timeout...");

        this.startActionTimeout();

        state = new GameSetupState(this, model);
    }

    private void startActionTimeout() {

        this.actionTimeoutThread = new Thread(() -> {
            this.beginTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - this.beginTime) < this.actionTimeout) {
                try {
                    Thread.sleep(1000);
                    Log.i("Remaining " + (this.actionTimeout / 1000L - this.getTimeout()) + " seconds");
                } catch (InterruptedException e) {
                    Log.d("ActionTimeoutThread was interrupted, well... This may be a problem.");
                    e.printStackTrace();
                }
            }

            this.state = this.state.handleUserTimeOutEvent();

            //this.update(new PlayerTimeoutEvent(this.getClass().getName(), this.model.getCurrentPlayerName()));
            //TODO: chiamare il metodo sullo stato corrente
            //TODO: inviare messaggio ai clients
        });

        this.actionTimeoutThread.start();

    }

    public int getTimeout() {
        return (int) ((System.currentTimeMillis() - this.beginTime) / 1000L);
    }

    private Tool[] pickToolCards() {
        ArrayList<Tool> tools = new ArrayList<>();
        tools.add(new CircularCutter());
        tools.add(new CopperReamer());
        tools.add(new CorkRow());
        tools.add(new DiamondPad());
        tools.add(new EglomiseBrush());
        tools.add(new FirmPastaDiluent());
        tools.add(new FirmPastaBrush());
        tools.add(new Gavel());
        tools.add(new Lathekin());
        tools.add(new ManualCutter());
        tools.add(new RoughingNipper());
        tools.add(new WheeledPincer());

        Collections.shuffle(tools);
        return tools.subList(0, Settings.TOOLCARDS_N).toArray(new Tool[Settings.TOOLCARDS_N]);
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
        this.startActionTimeout();

    }

    private void startTimeoutCommunicationThread() {
        this.timeoutCommunicationThread = new Thread(() -> {
            while (true) {

                // Adds a TimeoutCommunicationEvent to the event loop.
                this.outboundEventLoop.add(new TimeoutCommunicationEvent(this.getClass().getName(), this.model.getCurrentPlayerName(), this.getTimeout()));

                // Then waits for 1000ms
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    // TODO: What should I do if this thread gets interrupted?
                    Log.d("TimeoutCommunication Thread was interrupted");
                    // Should I restart it?
                    //this.timeoutCommunicationThread.start();
                }

            }
        });

        this.timeoutCommunicationThread.start();
    }

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
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                    // TODO: What should I do if this thread gets interrupted?
                    Log.d("eventLoopHandlerThread was interrupted");
                    // Should I restart it?
                    // this.eventLoopHandlerThread.start();
                }

            }
        });

        this.eventLoopHandlerThread.start();

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

}
