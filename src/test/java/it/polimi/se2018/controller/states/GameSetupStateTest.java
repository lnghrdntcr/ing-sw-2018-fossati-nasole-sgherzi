package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.controller.controllerEvent.ViewPlayerTimeoutEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.network.LocalProxy;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameSetupStateTest {


    private ArrayList<ArrayList<TestView>> views;
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
                this.views.get(i - Settings.MIN_NUM_PLAYERS).add(new TestView("Player" + j, i));
            }

            Controller actualController = new Controller(this.views.get(i - Settings.MIN_NUM_PLAYERS), 10000);

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
    public void startMessageTest() throws InterruptedException {
        for (int i = 0; i < this.games.size(); i++) {

            State actualTurnState = this.turnStates.get(i);
            Controller actualController = this.games.get(i);

            actualController.start();

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertTrue(tv.wasAskSchemaCardFaceDelivered);
                tv.dispatchEventToController(new SchemaCardSelectedEvent("test", "", tv.getPlayer(), 0, Side.FRONT));
            }

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertNotNull(actualController.getModel().getPlayerSchemacardFace(tv.getPlayer()));
                assertNotNull(actualController.getModel().getPlayerSchemaCopy(tv.getPlayer()));
            }

        }
    }

    @Test
    public void syncPlayer() throws InterruptedException {
        for (int i = 0; i < this.games.size(); i++) {

            State actualTurnState = this.turnStates.get(i);
            Controller actualController = this.games.get(i);

            actualController.start();

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertTrue(tv.wasAskSchemaCardFaceDelivered);
                tv.setConnected(false);
                tv.dispatchEventToController(new PlayerDisconnectedEvent("test", "", tv.getPlayer()));
                tv.wasAskSchemaCardFaceDelivered=false;
            }

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                actualController.reconnectPlayer(new FakeLocalProxy(), tv.getPlayer());
                tv.setConnected(true);
            }

            Thread.sleep(2000);

            for(TestView tv: views.get(i)) {
                assertTrue(tv.wasAskSchemaCardFaceDelivered);
            }

        }
    }

    @Test
    public void handleUserTimeOutEvent() throws InterruptedException {
        for (int i = 0; i < this.games.size(); i++) {

            State actualTurnState = this.turnStates.get(i);
            Controller actualController = this.games.get(i);

            actualController.start();

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertTrue(tv.wasAskSchemaCardFaceDelivered);
                tv.dispatchEventToController(new ViewPlayerTimeoutEvent("test", "", tv.getPlayer()));
            }

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertNotNull(actualController.getModel().getPlayerSchemacardFace(tv.getPlayer()));
                assertNotNull(actualController.getModel().getPlayerSchemaCopy(tv.getPlayer()));
            }

        }
    }


    @Test
    public void handleOtherEvents() throws InterruptedException {
        for (int i = 0; i < this.games.size(); i++) {

            Controller actualController = this.games.get(i);

            actualController.start();

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertTrue(tv.wasAskSchemaCardFaceDelivered);
                tv.dispatchEventToController(new CancelActionEvent("emitter", "", ""));
                tv.dispatchEventToController(new ChangeDiceNumberEvent("emitter", "", "", 1, 1, 1 ));
                tv.dispatchEventToController(new DiceActionEvent("emitter", "", "", 1, 1));
                tv.dispatchEventToController(new DoubleMoveDiceEvent("emitter", "", "", 1, new Point(1, 1), new Point(1, 1), new Point(1, 1), new Point(1, 1)));
                tv.dispatchEventToController(new DoubleMoveOfColorDiceEvent("emitter", "", "", 1, new Point(1, 1), new Point(1, 1), new Point(1, 1), new Point(1, 1), GameColor.RED));
                tv.dispatchEventToController(new EndTurnEvent("emitter", "", ""));
                tv.dispatchEventToController(new MoveDiceEvent("emitter", "", "", 2, new Point(1, 1), new Point(1, 1)));
                tv.dispatchEventToController(new PlaceAnotherDiceEvent("emitter", "", "", 2, new Point(1, 1), 1));
                tv.dispatchEventToController(new PlaceAnotherDiceSelectingNumberEvent("emitter", "", "", 2, new Point(1, 1), 1, 2));
                tv.dispatchEventToController(new PlaceDiceEvent("emitter", "", "", 2, new Point(1, 1)));
                tv.dispatchEventToController(new SwapDiceFaceWithDiceHolderEvent("emitter", "", "", 1, 1, 1, 1));
            }

            Thread.sleep(100);

            for(TestView tv: views.get(i)){
                assertNull(actualController.getModel().getPlayerSchemaCopy(tv.getPlayer()));
            }

        }

    }

    public class TestView extends View {

        private final int numPlayers;

        public boolean wasAskSchemaCardFaceDelivered=false;

        public TestView(String player, int numPlayers) {
            super(player);
            this.numPlayers = numPlayers;
        }

        @Override
        public void update(Event message) {
            //Log.d("TESTVIEW " + message.toString());
            if (message instanceof AskSchemaCardFaceEvent) {
                wasAskSchemaCardFaceDelivered=true;
            }
        }

        public void dispatchEventToController(ViewEvent event){
            notify(event);
        }

        public void setConnected(boolean bl){
            super.setConnected(bl);
        }


    }

    public class FakeLocalProxy extends LocalProxy{

        @Override
        public void sendEventToClient(Event event) {

        }
    }

}