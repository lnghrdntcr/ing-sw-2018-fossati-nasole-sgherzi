package it.polimi.se2018.controller;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.tool.*;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.objectives.*;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.Observer;
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

    public Controller (ArrayList<View> list){
        //register the Controller as an observer
        //and the view as an observer of the controller
        list.forEach((view) -> {
            view.register(this);
            register(view);
            model.register(view);
        });

        model = new GameTableMultiplayer(pickPublicObjectives(), , pickToolCards());


        //TODO: initializa state

    }

    private Tool[] pickToolCards() {
        ArrayList<Tool> tools = new ArrayList<>();
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
        return tools.subList(0, 3).toArray(new Tool[3]);
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
        return publicObjectives.subList(0, 3).toArray(new PublicObjective[3]);
    }

    private void connectViewWithModel(View view){
        this.model.register(view);
    }

    private void handleDicePlacement(Event e){

        PlaceDiceEvent event = (PlaceDiceEvent) e;
        if(model.getCurrentPlayerName().equals(event.getPlayerName())) {
            if (model.isDiceAllowed(event.getPlayerName(), event.getPoint(), model.getDiceFaceByIndex(event.getDiceFaceIndex()),
                    SchemaCardFace.Ignore.NOTHING)) {
                model.placeDice(event.getPlayerName(), event.getDiceFaceIndex(), event.getPoint());
            } else Log.w("Tried to place a dice in a not allowed position");
        }
        else {Log.w("Only the current player can place a dice");}
    }

    private void handleToolCardActivation(Event e){

        UseToolcardEvent event = (UseToolcardEvent) e;
        Tool tool = this.model.getToolCardByPosition(event.getToolCardIndex());
        int playerToken = this.model.getPlayerToken(event.getPlayerName());

        if(!this.model.getCurrentPlayerName().equals(event.getPlayerName())){
            Log.w("Only current player can use a toolcard");
            return;
        }

        if(!tool.isUsable()){
            Log.i(tool.getClass().getName() + "not usable in this turn.");
            return;
        }

        if(playerToken < tool.getNeededTokens()){
            Log.i(
                event.getPlayerName()
                + " cannot use the " + tool.getClass().getName() + " toolcard:\n "
                + "Needed:\t" + tool.getNeededTokens()
                + "\n Actual:\t" + playerToken
            );
            return;
        } else {
            this.model.useTokenOnToolcard(event.getPlayerName(), tool);
            // TODO: add a method in the model to expose the user using the toolcard, because tool.use() needs a player as argument.
            // TODO: call tool.use()
        }

    }

    private void handleTurnEnding(Event e){
        EndTurnEvent event = (EndTurnEvent) e;
        if(model.getCurrentPlayerName().equals(event.getPlayerName())){ model.nextTurn();}
        else{ Log.w("Only the current player can end its turn");}
    }

    private void handleSchemaCardSelection(Event e){

        SchemaCardSelectedEvent event = (SchemaCardSelectedEvent) e;
        String playerName = event.getPlayerName();
        // TODO: add a method in model to associate the selection of a SchemaCardFace with a player
    }

    @Override
    public void update(Event message) {
        state = state.handleEvent(message, model);
    }
}
