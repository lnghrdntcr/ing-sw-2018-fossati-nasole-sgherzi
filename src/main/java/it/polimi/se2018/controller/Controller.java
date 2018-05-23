package it.polimi.se2018.controller;

import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

import java.util.ArrayList;

public class Controller {

    GameTableMultiplayer model;

    public Controller (ArrayList<View> list){}


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


    }

    private void handleTurnEnding(Event e){
        EndTurnEvent event = (EndTurnEvent) e;
        if(model.getCurrentPlayerName().equals(event.getPlayerName())){ model.nextTurn();}
        else{ Log.w("Only the current player can end its turn");}
    }

    private void handleSchemaCardSelection(Event e){

    }


    public void update(){}

}
