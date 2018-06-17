package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.modelEvent.DraftBoardChangedEvent;
import it.polimi.se2018.model.modelEvent.PublicObjectiveEvent;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The state that handles the start of the game and does all the setup things
 */
public class GameSetupState extends State {
    private List<SchemaCard> schemaCardList;

    public GameSetupState(Controller controller, GameTableMultiplayer model) {
        super(controller, model);

        try {
            schemaCardList = SchemaCard.loadSchemaCardsFromJson(Settings.getSchemaCardDatabase());
        } catch (FileNotFoundException e) {
            Log.e("SchemaCardFile "+Settings.getSchemaCardDatabase() + " not valid!");
            return;
        }

        Collections.shuffle(schemaCardList);

        for(int i=0; i<getController().getPlayersList().length; i++){
            Event toDispatchEvent = new AskSchemaCardFaceEvent("GameSetupState",
                    getController().getPlayersList()[i],
                    schemaCardList.subList(i*2, i*2+2).toArray(new SchemaCard[2]));
            controller.dispatchEvent(toDispatchEvent);
        }
        // TODO: I'm not sure that doing this here is legit...
        model.onGameStart();
    }


    /**
     * Handle the selection of a SchemaCardFace by a player
     * @param event the event received
     * @return the new state of the game
     */
    @Override
    public State handleSchemaCardSelectedEvent(SchemaCardSelectedEvent event) {
        Log.d(getClass().getCanonicalName()+" handling SchemaCardSelectEvent");
        int playerIndex =-1;
        for(int i=0; i<getController().getPlayersList().length; i++) {
            if(getController().getPlayersList()[i].equals(event.getPlayerName())){
                playerIndex=i;
                break;
            }
        }

        if(playerIndex==-1) throw new IllegalArgumentException("Player not found: "+event.getPlayerName());

        getModel().setPlayerSchema(event.getPlayerName(), schemaCardList.get(playerIndex*2+event.getSchemaCardId()).getFace(event.getSide()));

        if(getModel().allPlayersHaveSelectedSchemaCardFace()){
            return new TurnState(getController(), getModel(),false, false);
        }


        return this;
    }

    @Override
    public State handleUserTimeOutEvent() {
        Log.d(getClass().getCanonicalName()+" handling UserTimeoutEvent");
        for(int i=0; i<getController().getPlayersList().length; i++) {
            if(getModel().getPlayerSchemacardFace(getController().getPlayersList()[i]) == null){
                //here the player does not have any schemacardface selected, selects the first card
                getModel().setPlayerSchema(getController().getPlayersList()[i], schemaCardList.get(i*2).getFace(Math.random()>0.5?Side.FRONT:Side.BACK));
            }
        }
        return new TurnState(getController(), getModel(),false, false);
    }
}
