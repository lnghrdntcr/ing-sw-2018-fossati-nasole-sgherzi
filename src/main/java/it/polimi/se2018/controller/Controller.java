package it.polimi.se2018.controller;

import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
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

/**
 * The main Controller class that handles the communication between the Model and the Views
 */
public class Controller extends Observable<Event> implements Observer<ViewEvent> {

    private GameTableMultiplayer model;
    private State state;

    private long actionTimeout;
    private long beginTime;
    private Thread actionTimeoutThread;

    public Controller(ArrayList<View> viewArrayList, long actionTimeout) {

        this.actionTimeout = actionTimeout;

        ArrayList<String> pln = new ArrayList<>();
        //register the Controller as an observer
        //and the view as an observer of the controller
        viewArrayList.forEach((view) -> {
            view.register(this);
            register(view);
            pln.add(view.getPlayer());
        });

        model = new GameTableMultiplayer(pickPublicObjectives(), pln.toArray(new String[viewArrayList.size()]), pickToolCards());

        viewArrayList.forEach(view -> model.register(view));

        Log.i("Game started with " + pln.size() + " players");


    }

    public void start() {

        if(state!=null) throw new IllegalStateException("Game already started!");

        Log.d("Starting timeout...");

        this.startActionTimeout();

        state = new GameSetupState(this, model);
    }

    private void startActionTimeout() {

        this.actionTimeoutThread = new Thread(() -> {
            this.beginTime = System.currentTimeMillis();
            while((System.currentTimeMillis() - this.beginTime) < this.actionTimeout){
                try {
                    Thread.sleep(1000);
                    Log.i("Remaining " + (this.actionTimeout / 1000L - this.getTimeout()) + " seconds");
                } catch (InterruptedException e) {
                    Log.d("ActionTimeoutThread was interrupted, well... This may be a problem.");
                    e.printStackTrace();
                }
            }

            //this.update(new PlayerTimeoutEvent(this.getClass().getName(), this.model.getCurrentPlayerName()));
            //TODO: chiamare il metodo sullo stato corrente
            //TODO: inviare messaggio ai clients
        });

        this.actionTimeoutThread.start();

    }

    public int getTimeout(){
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
        state = state.handleEvent(message, model);
        this.startActionTimeout();

    }


    public String[] getPlayersList(){
        return model.getPlayersName();
    }

    public void dispatchEvent(Event toDispatchEvent) {
        this.notify(toDispatchEvent);
    }

}
