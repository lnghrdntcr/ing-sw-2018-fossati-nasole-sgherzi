package it.polimi.se2018.controller;

import it.polimi.se2018.controller.states.GameSetupState;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.tool.*;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.objectives.*;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.*;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

import java.util.ArrayList;
import java.util.Collections;

public class Controller extends Observable<Event> implements Observer<Event> {

    private GameTableMultiplayer model;

    private State state;

    public Controller(ArrayList<View> viewArrayList) {
        ArrayList<String> pln = new ArrayList<>();
        //register the Controller as an observer
        //and the view as an observer of the controller
        viewArrayList.forEach((view) -> {
            view.register(this);
            register(view);
            model.register(view);
            pln.add(view.getPlayer());
        });


        model = new GameTableMultiplayer(pickPublicObjectives(), pln.toArray(new String[viewArrayList.size()]), pickToolCards());

    }

    public void start() {
        if(state!=null) throw new IllegalStateException("Game already started!");

        state=new GameSetupState(this);
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
    public void update(Event message) {
        state = state.handleEvent(message, model);
    }

    public String[] getPlayersList(){
        return model.getPlayersName();
    }

    public void dispatchEvent(Event toDispatchEvent) {
        this.notify(toDispatchEvent);
    }

}
