package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.MoveDiceEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnStateTest {

    ArrayList<View> views;
    ArrayList<Controller> games;
    ArrayList<GameTableMultiplayer> models;
    ArrayList<TurnState> turnStates;


    @Before
    public void setUp() throws Exception {
        // A: These were right after the first for loop. I think they belong here.
        this.games = new ArrayList<>();
        this.models = new ArrayList<>();
        this.turnStates = new ArrayList<>();

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {
            //I don't get what this is. I actually think it goes here,
            // because of the second for loop creating duplicates otherwise.
            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            Controller actualController = new Controller(this.views, 10);

            this.games.add(actualController);
            this.models.add(actualController.getModel());
            this.turnStates.add(
                    new TurnState(
                            actualController,
                            actualController.getModel(),
                            false,
                            false
                    ));
        }
    }

    @Test
    public void handleToolcardEvent() throws FileNotFoundException {

    }

    @Test
    public void handlePlaceDiceEvent() throws FileNotFoundException {

        for (int i = 0; i < this.games.size(); i++) {

            Controller actualController = this.games.get(i);
            GameTableMultiplayer actualModel = actualController.getModel();
            TurnState actualTurnState = this.turnStates.get(i);

            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);
            actualModel.drawDice();

            // Testing normal behaviour
            for (int j = 0; j < actualModel.getPlayersName().length; j++) {

                actualModel.setPlayerSchema("Player" + j, schemaCardFace);

                TurnState newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "Player" + j, 0, new Point(0, 0)));

                assertFalse(newState.isToolcardUsed());
                assertTrue(newState.isDicePlaced());

                // Trying to place a dice twice in the same turn.
                // In this case the returned state must be the SAME of the state before.
                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "Player" + j, 0, new Point(2, 2)));
                assertSame(actualTurnState, newState);

                // Testing against a dice which is in the same position.
                // In this case the returned state must be the SAME of the state before.
                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "Player" + j, 0, new Point(0, 0)));
                assertSame(newState, actualTurnState);

                actualModel.nextTurn();

                // Testing against another player trying to do the action.
                // In this case the returned state must be the SAME of the state before.
                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "Player" + j, 0, new Point(3, 2)));
                assertSame(newState, actualTurnState);

            }




        }

    }

    @Test
    public void handleUserTimeOutEvent() {
    }

    @Test
    public void handleEndTurnEvent() throws FileNotFoundException {
/*        for (int i = 0; i < this.games.size(); i++) {
            Controller actualController = this.games.get(i);
            GameTableMultiplayer actualModel = actualController.getModel();
            TurnState actualTurnState = this.turnStates.get(i);


            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);

            actualModel.drawDice();

            for (int j = 0; j < views.size(); j++) {

                try {
                    actualTurnState.handleEndTurnEvent(null);
                } catch (IllegalArgumentException e) {
                    Log.w("the event cant be null");
                }

                //can't test the "null model" exception...

                TurnState newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(
                this.getClass().getName(), "Player" + j, 0, new Point(0, 0)));
                assertTrue(newState.isDicePlaced());

                newState = (TurnState) actualTurnState.handleEndTurnEvent(new EndTurnEvent(this.getClass().getName(), "Player" + j));
                assertFalse(newState.isDicePlaced());
            }

            for(int j = 0; j < 2*views.size() * 9 + views.size(); j++){
                TurnState newState = (TurnState) actualTurnState.handleEndTurnEvent(new EndTurnEvent(this.getClass().getName(), "Player" + j));
            }
            assertFalse(actualModel.hasNextTurn());
        }*/
    }

    @Test
    public void isDicePlaced() {
    }

    @Test
    public void isToolcardUsed() {
    }
}