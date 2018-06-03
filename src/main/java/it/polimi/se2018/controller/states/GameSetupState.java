package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

/**
 * The state that handles the start of the game and does all the setup things
 */
public class GameSetupState extends State {
    private List<SchemaCard> schemaCardList;

    public GameSetupState(Controller controller) {
        super(controller);

        try {
            schemaCardList=SchemaCard.loadSchemaCardsFromJson(Settings.SCHEMACARD_DATABASE);
        } catch (FileNotFoundException e) {
            Log.e("SchemaCardFile "+Settings.SCHEMACARD_DATABASE + " not valid!");
            return;
        }

        Collections.shuffle(schemaCardList);

        for(int i=0; i<getController().getPlayersList().length; i++){
            Event toDispatchEvent = new AskSchemaCardFaceEvent("GameSetupState",
                    getController().getPlayersList()[i],
                    schemaCardList.subList(i*2, i*2+2).toArray(new SchemaCard[2]));
            controller.dispatchEvent(toDispatchEvent);
        }

    }

    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {

        if(event instanceof SchemaCardSelectedEvent) return handleSchemaCardSelection((SchemaCardSelectedEvent) event, model);

        return this;
    }

    /**
     * Handle the selection of a SchemaCardFace by a player
     * @param event the event received
     * @param model the model to modify
     * @return the new state of the game
     */
    private State handleSchemaCardSelection(SchemaCardSelectedEvent event, GameTableMultiplayer model) {

        int playerIndex =-1;
        for(int i=0; i<getController().getPlayersList().length; i++) {
            if(getController().getPlayersList()[i].equals(event.getPlayerName())){
                playerIndex=i;
                break;
            }
        }

        if(playerIndex==-1) throw new IllegalArgumentException("Player not found: "+event.getPlayerName());

        model.setPlayerSchema(event.getPlayerName(), schemaCardList.get(playerIndex*2+event.getSchemaCardId()).getFace(event.getSide()));

        if(model.allPlayersHaveSelectedSchemaCardFace()){
            return new TurnState(getController(), false, false);
        }

        return this;
    }
}
