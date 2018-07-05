package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.controller.controllerEvent.LogEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.modelEvent.SchemaChangedEvent;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
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

    public GameSetupState(Controller controller, GameTableMultiplayer model) {
        super(controller, model);

        try {
            schemaCardList = SchemaCard.loadSchemaCardsFromJson(Settings.getDefaultSchemaCardDatabase());
        } catch (FileNotFoundException e) {
            Log.e("SchemaCardFile " + Settings.getDefaultSchemaCardDatabase() + " not valid!");
            return;
        }

        Collections.shuffle(schemaCardList);

        for (int i = 0; i < getController().getPlayersList().length; i++) {
            Event toDispatchEvent = new AskSchemaCardFaceEvent("GameSetupState",
                getController().getPlayersList()[i],
                getController().getPlayersList()[i],
                schemaCardList.subList(i * 2, i * 2 + 2).toArray(new SchemaCard[2]));
            controller.dispatchEvent(toDispatchEvent);
        }


    }


    /**
     * Performs all the necessary action to sync the current game state and a reconnected user
     * @param playerName the player to resync
     */
    @Override
    public void syncPlayer(String playerName) {

        if (getModel().getPlayerSchemacardFace(playerName) == null) {

            for (int i = 0; i < getController().getPlayersList().length; i++) {
                if (getController().getPlayersList()[i].equals(playerName)) {
                    Event toDispatchEvent = new AskSchemaCardFaceEvent("GameSetupState",
                        getController().getPlayersList()[i],
                        getController().getPlayersList()[i],
                        schemaCardList.subList(i * 2, i * 2 + 2).toArray(new SchemaCard[2]));
                    getController().dispatchEvent(toDispatchEvent);
                }
            }
        }

    }


    /**
     * Handle the selection of a SchemaCardFace by a player
     *
     * @param event the event received
     * @return the new state of the game
     */
    @Override
    public State handleSchemaCardSelectedEvent(SchemaCardSelectedEvent event) {
        Log.d(getClass().getCanonicalName() + " handling SchemaCardSelectEvent");
        int playerIndex = -1;
        for (int i = 0; i < getController().getPlayersList().length; i++) {
            if (getController().getPlayersList()[i].equals(event.getPlayerName())) {
                playerIndex = i;
                break;
            }
        }

        if (playerIndex == -1) throw new IllegalArgumentException("Player not found: " + event.getPlayerName());

        getModel().setPlayerSchema(event.getPlayerName(), schemaCardList.get(playerIndex * 2 + event.getSchemaCardId()).getFace(event.getSide()));

        // Sends the appropriate combination of <Player, Schema> to the view.
        getController().dispatchEvent(new SchemaChangedEvent(this.getClass().getName(), "", event.getPlayerName(), new Schema(schemaCardList.get(playerIndex * 2 + event.getSchemaCardId()).getFace(event.getSide()))));

        getController().dispatchEvent(new LogEvent(this.getClass().getName(), event.getPlayerName(), "", event.getPlayerName() + " has selected " + schemaCardList.get(playerIndex * 2 + event.getSchemaCardId()).getFace(event.getSide()).getName() + "!"));

        if (getModel().allPlayersHaveSelectedSchemaCardFace()) {
            return new TurnState(getController(), getModel(), false, false);
        }


        return this;
    }

    /**
     * Handles the timeout for the current player, doing the default action
     *
     * @return the new state of the game
     */
    @Override
    public State handleUserTimeOutEvent() {

        Log.d(getClass().getCanonicalName() + " handling UserTimeoutEvent");

        for (int i = 0; i < getController().getPlayersList().length; i++) {

            if (getModel().getPlayerSchemacardFace(getController().getPlayersList()[i]) == null) {

                //here the player does not have any schemacardface selected, selects the first card

                String player = getController().getPlayersList()[i];

                SchemaCardFace schema = schemaCardList.get(i * 2).getFace(Math.random() > 0.5 ? Side.FRONT : Side.BACK);

                getModel().setPlayerSchema(player, schema);

                // Send the event to the view.
                getController().dispatchEvent(new SchemaChangedEvent(this.getClass().getName(), "", player, new Schema(schema)));

            }
        }
        return new TurnState(getController(), getModel(), false, false);
    }
}
