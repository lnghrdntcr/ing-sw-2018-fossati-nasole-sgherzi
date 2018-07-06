package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.View;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.SchemaCardSelectedEvent;
import it.polimi.se2018.view.viewEvent.ViewEvent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameEndStateTest {

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

            Controller actualController = new Controller(this.views.get(i - Settings.MIN_NUM_PLAYERS), 100000);

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
    public void endMessageTest() throws IOException, InterruptedException {

        try {
            Files.delete(Paths.get("leaderboad.json"));
        } catch (IOException ignored) {
        }

        for (int i = 0; i < this.games.size(); i++) {

            State actualTurnState = this.turnStates.get(i);
            Controller actualController = this.games.get(i);

            actualController.start();

            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);


            GameTableMultiplayer actualModel = actualController.getModel();

            for (int j = 0; j < i + Settings.MIN_NUM_PLAYERS; j++) {
                actualModel.setPlayerSchema("Player" + j, schemaCardFace);
            }

            for (int j = 0; j < 2 * views.get(i).size() * (Settings.TURNS) + views.get(i).size(); j++) {
                actualTurnState = actualTurnState.handleEndTurnEvent(new EndTurnEvent(this.getClass().getName(), actualModel.getCurrentPlayerName(), ""));
                actualTurnState = actualTurnState.handleSchemaCardSelectedEvent(new SchemaCardSelectedEvent("emitter", "",  views.get(i).get(0).getPlayer(), 0, Side.FRONT));
            }

            assertTrue(actualTurnState instanceof GameEndState);

            GameEndState ges = (GameEndState) actualTurnState;

            actualTurnState.syncPlayer("sdfg");

            Thread.sleep(1000);


        }




    }

    public class TestView extends View {

        private final int numPlayers;

        public TestView(String player, int numPlayers) {
            super(player);
            this.numPlayers = numPlayers;
        }

        public void dispatchEventToController(ViewEvent event){
            notify(event);
        }

        @Override
        public void update(Event message) {
            Log.d("TESTVIEW " + message.toString());
            if (message instanceof EndGameEvent) {
                EndGameEvent ev = (EndGameEvent) message;
                assertEquals(numPlayers, ev.getLeaderBoard().size());
            }
        }
    }

}