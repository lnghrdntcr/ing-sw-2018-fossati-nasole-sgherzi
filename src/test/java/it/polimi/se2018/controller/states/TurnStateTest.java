package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceEvent;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceWithNumberSelectionEvent;
import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TurnStateTest {

    private ArrayList<ArrayList<View>> views;
    private ArrayList<Controller> games;
    private ArrayList<GameTableMultiplayer> models;
    private ArrayList<TurnState> turnStates;


    @Before
    public void setUp() throws Exception {
        // A: These were right after the first for loop. I think they belong here.
        this.games = new ArrayList<>();
        this.models = new ArrayList<>();
        this.turnStates = new ArrayList<>();
        this.views = new ArrayList<>();

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {
            this.views.add(new ArrayList<>());

            for (int j = 0; j < i; j++) {
                this.views.get(i - Settings.MIN_NUM_PLAYERS).add(new RemoteView("Player" + j, RemoteView.Graphics.CLI));
            }

            Controller actualController = new Controller(this.views.get(i - Settings.MIN_NUM_PLAYERS), 10);

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
        for (int i = 0; i < this.games.size(); i++) {

            Controller actualController = this.games.get(i);
            GameTableMultiplayer actualModel = actualController.getModel();
            TurnState actualTurnState = this.turnStates.get(i);


            try {
                actualTurnState.handleEndTurnEvent(null);
                fail();
            } catch (IllegalArgumentException ignore) {
                Log.i("Null event thrown as expected.");
            }

            //player trying to use tool while not its turn
            actualTurnState.handleToolcardEvent(new MoveDiceEvent(getClass().getName(), "", actualModel.getPlayersName()[1],
                    1, new Point(0, 0), new Point(1, 1)));

            actualTurnState.handleToolcardEvent(new MoveDiceEvent(getClass().getName(), "", actualModel.getCurrentPlayerName(),
                    1, new Point(0, 0), new Point(1, 1)));


        }

    }


    @Test
    public void handlePlaceDiceEvent() throws FileNotFoundException {

        for (int i = 0; i < this.games.size(); i++) {

            Controller actualController = this.games.get(i);
            GameTableMultiplayer actualModel = actualController.getModel();
            TurnState actualTurnState = this.turnStates.get(i);

            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);
            //actualModel.drawDice();

            // Testing normal behaviour
            for (int j = 0; j < actualModel.getPlayersName().length; j++) {

                actualModel.setPlayerSchema("Player" + j, schemaCardFace);

                TurnState newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "", "Player" + j, 0, new Point(0, 0)));

                assertFalse(newState.isToolcardUsed());
                assertTrue(newState.isDicePlaced());

                // Trying to place a dice twice in the same turn.
                // In this case the returned state must be the SAME of the state before.
                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "", "Player" + j, 0, new Point(2, 2)));
                assertSame(actualTurnState, newState);

                // Testing against a dice which is in the same position.
                // In this case the returned state must be the SAME of the state before.
                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "", "Player" + j, 0, new Point(0, 0)));
                assertSame(newState, actualTurnState);

                actualModel.nextTurn();

                // Testing against another player trying to do the action.
                // In this case the returned state must be the SAME of the state before.
                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(this.getClass().getName(), "", "Player" + j, 0, new Point(3, 2)));
                assertSame(newState, actualTurnState);

            }


        }

    }


    @Test
    public void handlePlayerDisconnected() {
        for (int i = 0; i < this.games.size(); i++) {

            Controller actualController = this.games.get(i);
            GameTableMultiplayer actualModel = actualController.getModel();
            TurnState actualTurnState = this.turnStates.get(i);

            //normal behavior
            actualTurnState.handlePlayerDisconnected(new PlayerDisconnectedEvent(getClass().getName(), actualModel.getCurrentPlayerName(), ""));
            //someone but the current player is trying to call it
            actualTurnState.handlePlayerDisconnected(new PlayerDisconnectedEvent(getClass().getName(), actualModel.getPlayersName()[1], ""));

        }
    }


    @Test
    public void handleEndTurnEvent() throws FileNotFoundException {
        for (int i = 0; i < this.games.size(); i++) {
            Controller actualController = this.games.get(i);
            GameTableMultiplayer actualModel = actualController.getModel();
            TurnState actualTurnState = this.turnStates.get(i);


            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);

            //not current player trying to invoke the method
            TurnState newState = (TurnState) actualTurnState.handleEndTurnEvent(new EndTurnEvent(this.getClass().getName(),
                    actualModel.getPlayersName()[1], ""));
            assertEquals(newState, actualTurnState);

            for (int j = 0; j < views.get(i).size(); j++) {

                actualModel.setPlayerSchema("Player" + j, schemaCardFace);

                //can't test the "null model" exception...

                newState = (TurnState) actualTurnState.handlePlaceDiceEvent(new PlaceDiceEvent(
                        this.getClass().getName(), "", actualModel.getCurrentPlayerName(), 1, new Point(0, 0)));
                assertTrue(newState.isDicePlaced());

                newState = (TurnState) actualTurnState.handleEndTurnEvent(new EndTurnEvent(this.getClass().getName(),
                        actualModel.getCurrentPlayerName(), ""));
                assertFalse(newState.isDicePlaced());
            }

            for (int j = 0; j < 2 * views.get(i).size() * (Settings.TURNS - 1) + views.get(i).size(); j++) {
                newState = (TurnState) actualTurnState.handleEndTurnEvent(new EndTurnEvent(this.getClass().getName(), actualModel.getCurrentPlayerName(), ""));
            }
            assertFalse(actualModel.hasNextTurn());
        }
    }


    @Test
    public void toolcardTest_1_10_6() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"RoughingNipper", "DiamondPad", "FirmPastaBrush"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //game it's now started

        //==========RoughingNipper==========

        DiceFace diceFace = actualController.getModel().getDiceFaceByIndex(0);

        int direction = 1;
        if (diceFace.getNumber() == 6) direction = -1;

        TestView testView = views.get(0);


        //WRONG PLAYER
        views.get(1).dispatchEventToController(new ChangeDiceNumberEvent("test", "", views.get(1).getPlayer(), 0, 0, direction));
        Thread.sleep(200);
        assertEquals(diceFace.getColor(), actualController.getModel().getDiceFaceByIndex(0).getColor());
        assertEquals(diceFace.getNumber(), actualController.getModel().getDiceFaceByIndex(0).getNumber());
        assertEquals(0, actualController.getModel().getToolCardByPosition(0).getToken());

        //CORRECT PLAYER
        testView.dispatchEventToController(new ChangeDiceNumberEvent("test", "", testView.getPlayer(), 0, 0, direction));
        Thread.sleep(200);
        assertEquals(diceFace.getColor(), actualController.getModel().getDiceFaceByIndex(actualController.getModel().getDiceNumberOnDraftBoard() - 1).getColor());
        assertEquals(diceFace.getNumber() + direction, actualController.getModel().getDiceFaceByIndex(actualController.getModel().getDiceNumberOnDraftBoard() - 1).getNumber());
        assertEquals(1, actualController.getModel().getToolCardByPosition(0).getToken());


        //ALREADY USED
        diceFace = actualController.getModel().getDiceFaceByIndex(0);
        testView.dispatchEventToController(new ChangeDiceNumberEvent("test", "", testView.getPlayer(), 0, 0, direction));
        Thread.sleep(200);
        assertEquals(diceFace.getColor(), actualController.getModel().getDiceFaceByIndex(0).getColor());
        assertEquals(diceFace.getNumber(), actualController.getModel().getDiceFaceByIndex(0).getNumber());
        assertEquals(1, actualController.getModel().getToolCardByPosition(0).getToken());

        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));


        //==========DiamondPad==========
        //Next player
        testView = views.get(1);
        diceFace = actualController.getModel().getDiceFaceByIndex(0);
        testView.dispatchEventToController(new DiceActionEvent("test", "", testView.getPlayer(), 1, 0));
        Thread.sleep(200);
        assertEquals(diceFace.getColor(), actualController.getModel().getDiceFaceByIndex(actualController.getModel().getDiceNumberOnDraftBoard() - 1).getColor());
        assertEquals(7 - diceFace.getNumber(), actualController.getModel().getDiceFaceByIndex(actualController.getModel().getDiceNumberOnDraftBoard() - 1).getNumber());
        assertEquals(1, actualController.getModel().getToolCardByPosition(1).getToken());
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));


        //==========FirmPastaBrush==========
        testView = views.get(1); //still second player
        diceFace = actualController.getModel().getDiceFaceByIndex(0);
        int oldDices = actualController.getModel().getDiceNumberOnDraftBoard();
        testView.dispatchEventToController(new DiceActionEvent("test", "", testView.getPlayer(), 2, 0));
        Thread.sleep(500);

        assertEquals(oldDices - 1, actualController.getModel().getDiceNumberOnDraftBoard());


        assertEquals(diceFace.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getColor());


        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));
    }


    @Test
    public void toolcardTest_5_7_9() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"CircularCutter", "Gavel", "CorkRow"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //==========CircularCutter==========
        views.get(0).dispatchEventToController(new EndTurnEvent("test", views.get(0).getPlayer(), ""));
        views.get(1).dispatchEventToController(new EndTurnEvent("test", views.get(1).getPlayer(), ""));
        views.get(1).dispatchEventToController(new EndTurnEvent("test", views.get(1).getPlayer(), ""));
        views.get(0).dispatchEventToController(new EndTurnEvent("test", views.get(0).getPlayer(), ""));

        Thread.sleep(1000);

        DiceFace oldDiceFaceOnDraftBoard = actualController.getModel().getDiceFaceByIndex(0);
        DiceFace oldDiceFaceOnTurnOlder = actualController.getModel().getImmutableDiceHolder().getDiceFaces(0)[0];


        TestView testView = views.get(1); //1 turn
        testView.dispatchEventToController(new SwapDiceFaceWithDiceHolderEvent("test", "", testView.getPlayer(), 0, 0, 0, 0));
        Thread.sleep(500);

        DiceFace newDiceFaceOnDraftBoard = actualController.getModel().getDiceFaceByIndex(actualController.getModel().getDiceNumberOnDraftBoard() - 1);
        DiceFace newDiceFaceOnTurnOlder = actualController.getModel().getImmutableDiceHolder().getDiceFaces(0)[actualController.getModel().getImmutableDiceHolder().getDiceFaces(0).length - 1];


        assertEquals(oldDiceFaceOnDraftBoard.getColor(), newDiceFaceOnTurnOlder.getColor());
        assertEquals(oldDiceFaceOnDraftBoard.getNumber(), newDiceFaceOnTurnOlder.getNumber());

        assertEquals(oldDiceFaceOnTurnOlder.getColor(), newDiceFaceOnDraftBoard.getColor());
        assertEquals(oldDiceFaceOnTurnOlder.getNumber(), newDiceFaceOnDraftBoard.getNumber());
        assertEquals(1, actualController.getModel().getToolCardByPosition(0).getToken());
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        //==========Gavel==========
        testView = views.get(0);
        ArrayList<DiceFace> diceFaces = new ArrayList<>();
        for (int i = 0; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
            diceFaces.add(actualController.getModel().getDiceFaceByIndex(i));
        }
        testView.dispatchEventToController(new DiceActionEvent("test", "", testView.getPlayer(), 1, -1));

        Thread.sleep(1000);
        assertEquals(0, actualController.getModel().getToolCardByPosition(1).getToken());
        //cannot use in first turn
        for (int i = 0; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
            assertEquals(diceFaces.get(i).getColor(), actualController.getModel().getDiceFaceByIndex(i).getColor());
            assertEquals(diceFaces.get(i).getNumber(), actualController.getModel().getDiceFaceByIndex(i).getNumber());
        }
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));


        testView.dispatchEventToController(new DiceActionEvent("test", "", testView.getPlayer(), 1, -1));
        Thread.sleep(1000);

        //at least one change....?
        int changes = 0;
        for (int i = 0; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
            if (diceFaces.get(i).getColor() != actualController.getModel().getDiceFaceByIndex(i).getColor()) changes++;
            if (diceFaces.get(i).getNumber() != actualController.getModel().getDiceFaceByIndex(i).getNumber())
                changes++;
        }

        if (changes == 0) {
            Log.w("0 changes after Gavel, it can be, but try to run the tests again for correctness!");
        }
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));
        Log.i("PLAYERSCHEMACARDFACE " + actualController.getModel().getPlayerSchemacardFace(testView.getPlayer()).getName());
        Thread.sleep(1000);
        //==========CorkRow==========
        testView = views.get(1);
        DiceFace diceFace = actualController.getModel().getDiceFaceByIndex(0);
        testView.dispatchEventToController(new PlaceAnotherDiceEvent("test", "", testView.getPlayer(), 2, new Point(2, 2), 0));
        Thread.sleep(1000);
        assertEquals(diceFace.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(2, 2)).getNumber());
        assertEquals(diceFace.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(2, 2)).getColor());
    }


    @Test
    public void toolcardTest_2() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"EglomiseBrush", "CopperReamer", "Lathekin"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //==========EglomiseBrush==========
        int secondIndex = -1;
        do {
            actualController.getModel().redrawAllDice();
            for (int i = 1; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
                if (actualController.getModel().getDiceFaceByIndex(0).getColor() != actualController.getModel().getDiceFaceByIndex(i).getColor() &&
                        actualController.getModel().getDiceFaceByIndex(0).getNumber() != actualController.getModel().getDiceFaceByIndex(i).getNumber()) {
                    secondIndex = i;
                }
            }
        } while (secondIndex == -1);

        DiceFace diceFace1 = actualController.getModel().getDiceFaceByIndex(0);
        DiceFace diceFace2 = actualController.getModel().getDiceFaceByIndex(secondIndex);

        TestView testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), 0, new Point(0, 0)));
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        testView = views.get(1);
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), secondIndex - 1, new Point(1, 0)));

        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getColor());


        testView.dispatchEventToController(new MoveDiceEvent("test", "", testView.getPlayer(), 0, new Point(0, 0), new Point(1, 1)));
        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 1)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 1)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getColor());
    }


    @Test
    public void toolcardTest_3() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"EglomiseBrush", "CopperReamer", "Lathekin"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //==========CopperReamer==========
        int secondIndex = -1;
        do {
            actualController.getModel().redrawAllDice();
            for (int i = 1; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
                if (actualController.getModel().getDiceFaceByIndex(0).getColor() != actualController.getModel().getDiceFaceByIndex(i).getColor() &&
                        actualController.getModel().getDiceFaceByIndex(0).getNumber() != actualController.getModel().getDiceFaceByIndex(i).getNumber()) {
                    secondIndex = i;
                }
            }
        } while (secondIndex == -1);

        DiceFace diceFace1 = actualController.getModel().getDiceFaceByIndex(0);
        DiceFace diceFace2 = actualController.getModel().getDiceFaceByIndex(secondIndex);

        TestView testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), 0, new Point(0, 0)));
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        testView = views.get(1);
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), secondIndex - 1, new Point(1, 0)));

        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getColor());


        testView.dispatchEventToController(new MoveDiceEvent("test", "", testView.getPlayer(), 1, new Point(0, 0), new Point(1, 1)));
        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 1)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 1)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getColor());
    }

    @Test
    public void toolcardTest_4() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"EglomiseBrush", "CopperReamer", "Lathekin"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //==========Lathekin==========
        int secondIndex = -1;
        do {
            actualController.getModel().redrawAllDice();
            for (int i = 1; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
                if (actualController.getModel().getDiceFaceByIndex(0).getColor() != actualController.getModel().getDiceFaceByIndex(i).getColor() &&
                        actualController.getModel().getDiceFaceByIndex(0).getNumber() != actualController.getModel().getDiceFaceByIndex(i).getNumber()) {
                    secondIndex = i;
                }
            }
        } while (secondIndex == -1);

        DiceFace diceFace1 = actualController.getModel().getDiceFaceByIndex(0);
        DiceFace diceFace2 = actualController.getModel().getDiceFaceByIndex(secondIndex);

        TestView testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), 0, new Point(0, 0)));
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        testView = views.get(1);
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));
        testView.dispatchEventToController(new EndTurnEvent("test", testView.getPlayer(), ""));

        testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), secondIndex - 1, new Point(1, 0)));

        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getColor());


        testView.dispatchEventToController(new DoubleMoveDiceEvent("test", "", testView.getPlayer(), 2, new Point(0, 0), new Point(1, 1), new Point(1, 0), new Point(2, 1)));
        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 1)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 1)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(2, 1)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(2, 1)).getColor());
    }


    @Test
    public void toolcardTest_8() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"WheeledPincer", "FirmPastaDiluent", "ManualCutter"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //==========WheeledPincer==========
        int secondIndex = -1;
        do {
            actualController.getModel().redrawAllDice();
            for (int i = 1; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
                if (actualController.getModel().getDiceFaceByIndex(0).getColor() != actualController.getModel().getDiceFaceByIndex(i).getColor() &&
                        actualController.getModel().getDiceFaceByIndex(0).getNumber() != actualController.getModel().getDiceFaceByIndex(i).getNumber()) {
                    secondIndex = i;
                }
            }
        } while (secondIndex == -1);

        DiceFace diceFace1 = actualController.getModel().getDiceFaceByIndex(0);
        DiceFace diceFace2 = actualController.getModel().getDiceFaceByIndex(secondIndex);

        TestView testView = views.get(0);
        testView.dispatchEventToController(new PlaceDiceEvent("test", "", testView.getPlayer(), 0, new Point(0, 0)));
        Thread.sleep(500);


        testView.dispatchEventToController(new PlaceAnotherDiceEvent("test", "", testView.getPlayer(), 0, new Point(1, 0), secondIndex-1));

        Thread.sleep(500);

        assertEquals(diceFace1.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getNumber());
        assertEquals(diceFace1.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getColor());

        assertEquals(diceFace2.getNumber(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getNumber());
        assertEquals(diceFace2.getColor(), actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(1, 0)).getColor());


    }

    @Test
    public void toolcardTest_11() throws InterruptedException {


        ArrayList<TestView> views = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            views.add(new TestView("Player" + j, 2));
        }

        Controller actualController = new Controller(views, 100000, new String[]{"WheeledPincer", "FirmPastaDiluent", "ManualCutter"});
        actualController.start();

        Thread.sleep(100);

        for (TestView tv : views) {
            assertTrue(tv.wasAskSchemaCardFaceDelivered);
            tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
        }

        Thread.sleep(100);

        //==========FirmPastaDiluent==========
        TestView testView = views.get(0);
        ArrayList<DiceFace> diceFaces = new ArrayList<>();
        for (int i = 0; i < actualController.getModel().getDiceNumberOnDraftBoard(); i++) {
            diceFaces.add(actualController.getModel().getDiceFaceByIndex(i));
        }




        testView.dispatchEventToController(new DiceActionEvent("test", "", testView.getPlayer(), 1, 0));
        Thread.sleep(200);

        //other dices remain the same
        for (int i = 0; i < actualController.getModel().getDiceNumberOnDraftBoard()-1; i++) {
            assertEquals(diceFaces.get(i+1).getColor(), actualController.getModel().getDiceFaceByIndex(i).getColor());
            assertEquals(diceFaces.get(i+1).getNumber(), actualController.getModel().getDiceFaceByIndex(i).getNumber());
        }

        if(testView.placed){
            assertNotNull(actualController.getModel().getPlayerSchemaCopy(testView.getPlayer()).getDiceFace(new Point(0, 0)).getColor());
        }




    }


    public class TestView extends View {

        private final int numPlayers;


        public boolean placed=false;

        public boolean wasAskSchemaCardFaceDelivered = false;

        public TestView(String player, int numPlayers) {
            super(player);
            this.numPlayers = numPlayers;
        }

        @Override
        public void update(Event message) {
            if (!message.getReceiver().equals(getPlayer())) return;
            //Log.d("TESTVIEW " + message.toString());
            if (message instanceof AskSchemaCardFaceEvent) {
                wasAskSchemaCardFaceDelivered = true;
            }

            if (message instanceof AskPlaceRedrawDiceEvent) {
                AskPlaceRedrawDiceEvent event = (AskPlaceRedrawDiceEvent) message;
                Log.i("Replying to AskPlaceRedrawDiceEvent");
                dispatchEventToController(new PlaceAnotherDiceEvent("test", "", getPlayer(), 2, new Point(0, 0), event.getDiceIndex()));
            }

            if(message instanceof AskPlaceRedrawDiceWithNumberSelectionEvent){
                AskPlaceRedrawDiceWithNumberSelectionEvent event = (AskPlaceRedrawDiceWithNumberSelectionEvent) message;
                Log.i("Replying to AskPlaceRedrawDiceEvent");
                dispatchEventToController(new PlaceAnotherDiceSelectingNumberEvent("test", "", getPlayer(), 2, new Point(0, 0), event.getDiceIndex(), 1));
                placed=true;
            }
        }

        public void dispatchEventToController(ViewEvent event) {
            notify(event);
        }

        public void setConnected(boolean bl) {
            super.setConnected(bl);
        }


    }

}