package it.polimi.se2018.model;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.View;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTableMultiplayerTest {

    GameTableMultiplayer model;
    Controller controller;
    ArrayList<View> views;

    @Test
    public void getCurrentPlayerName() {

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            for (int j = 0; j < i; j++) {
                assertEquals("Player" + j, this.model.getCurrentPlayerName());
                this.model.nextTurn();
            }

        }
    }

    @Test
    public void getToolCardByPosition() {
        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // As the "truthfulness" of the ToolCards cannot be tested (they are picked up random)
            // I can only test that every time that i call .getToolcardByPosition a non-null toolcard is returned.
            try {
                this.model.getToolCardByPosition(Settings.TOOLCARDS_N);
                fail();
            } catch (IllegalArgumentException ignored) {
            }

            try {
                this.model.getToolCardByPosition(-1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }

            for (int j = 0; j < Settings.TOOLCARDS_N; j++) {
                assertNotNull(this.model.getToolCardByPosition(j));
            }

        }

    }

    @Test
    public void getPublicObjectiveCardByPosition() {

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // As the "truthfulness" of the PublicObjectivesCard cannot be tested (they are picked up random)
            // I can only test that every time that i call .getPublicObjectiveByPosition a non-null toolcard is returned.

            // Testing against a non legal input.
            try {
                this.model.getPublicObjectiveCardByPosition(Settings.POBJECTIVES_N);
                fail();
            } catch (IllegalArgumentException ignored) {
            }

            try {
                this.model.getPublicObjectiveCardByPosition(-1);
                fail();
            } catch (IllegalArgumentException ignored) {
            }

            // Testing normal behaviour
            for (int j = 0; j < Settings.POBJECTIVES_N; j++) {
                assertNotNull(this.model.getPublicObjectiveCardByPosition(j));
            }

        }

    }

    @Test
    public void computeAllScores() throws FileNotFoundException {

        // This method cannot be tested as the player PrivateObjective is Randomized each time.
        // Let's hope for good.

    }

    @Test
    public void getPlayerToken() throws FileNotFoundException {

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);

            for (int j = 0; j < i; j++) {

                this.model.setPlayerSchema("Player" + j, schemaCardFace);
                assertEquals(schemaCardFace.getDifficulty(), this.model.getPlayerToken("Player" + j));

            }


        }


    }


    @Test
    public void useTokenOnToolcard() {
    }

    @Test
    public void increaseDecreaseDice() {
    }

    @Test
    public void swapDraftDiceWithHolder() {
    }

    @Test
    public void redrawDice() {
    }

    @Test
    public void redrawAllDice() {
    }

    @Test
    public void flipDice() {
    }

    @Test
    public void drawDice() {
    }

    @Test
    public void putBackAndRedrawDice() {
    }

    @Test
    public void changeDiceNumber() {
    }

    @Test
    public void moveDice() {
    }

    @Test
    public void hasNextTurn() {
    }

    @Test
    public void nextTurn() {
    }

    @Test
    public void isDiceAllowed() {
    }

    @Test
    public void isAloneDiceAllowed() {
    }

    @Test
    public void getDiceFaceByIndex() {
    }

    @Test
    public void setPlayerSchema() {
    }

    @Test
    public void getPlayersName() {
    }

    @Test
    public void allPlayersHaveSelectedSchemaCardFace() {
    }

    @Test
    public void getPlayerDiceFace() {
    }

    @Test
    public void isFirstTurnInRound() {
    }

    @Test
    public void getPlayerSchemaCopy() {
    }

    @Test
    public void isColorInDiceHolder() {
    }

    @Test
    public void playerWillDropTurn() {
    }

    @Test
    public void getDiceNumberOnDraftBoard() {
    }

    @Test
    public void getPlayerSchemacardFace() {
    }
}