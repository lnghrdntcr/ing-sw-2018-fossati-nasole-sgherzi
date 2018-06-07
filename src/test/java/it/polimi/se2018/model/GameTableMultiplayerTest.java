package it.polimi.se2018.model;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.View;
import org.junit.Test;
import sun.font.FontRunIterator;

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
    public void useTokenOnToolcard() throws FileNotFoundException {

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Setting the schema card face is needed as the player has to get the token before using them. (u don't say?)
            SchemaCardFace schemaCardFace = SchemaCard.loadSchemaCardsFromJson("gameData/tests/validTest_emptycard.scf").get(0).getFace(Side.FRONT);

            // For every toolcard
            for (int k = 0; k < Settings.TOOLCARDS_N; k++) {

                Tool currentToolcard = this.model.getToolCardByPosition(k);
                int currentUsedTokenForToolcard = 0;

                // For every player
                for (int j = 0; j < i; j++) {

                    int currentTokenForPlayer = schemaCardFace.getDifficulty();

                    // If it is the first time that a toolcard is used...

                    if (this.model.getPlayerToken("Player" + j) >= currentToolcard.getNeededTokens()) {

                        if (currentToolcard.getNeededTokens() == 1) {
                            currentUsedTokenForToolcard++;
                            currentTokenForPlayer--;
                        } else {
                            currentUsedTokenForToolcard += 2;
                            currentTokenForPlayer -= 2;
                        }

                        this.model.useTokenOnToolcard("Player" + j, currentToolcard);
                        assertEquals(currentTokenForPlayer, this.model.getPlayerToken("Player" + j));

                    }

                    assertEquals(currentUsedTokenForToolcard, currentToolcard.getToken());

                }

            }


        }
    }

    @Test
    public void increaseDecreaseDice() {

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Draw the dices
            this.model.drawDice();

            for (int j = 0; j < this.model.getDiceNumberOnDraftBoard(); j++) {

                DiceFace df = this.model.getDiceFaceByIndex(j);

                // Testing against an illegal direction
                try {
                    this.model.increaseDecreaseDice(j, -22);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }

                // Testing normal behaviour.
                if (df.getNumber() != 6 && df.getNumber() != 1) {
                    this.model.increaseDecreaseDice(j, -1);
                    assertEquals(df.getNumber() - 1, this.model.getDiceFaceByIndex(this.model.getDiceNumberOnDraftBoard() - 1).getNumber());
                } else if (df.getNumber() == 6) {
                    try {
                        this.model.increaseDecreaseDice(j, 1);
                        fail();
                    } catch (IllegalStateException ignored) {
                    }
                } else if (df.getNumber() == 1) {

                    try {
                        this.model.increaseDecreaseDice(j, -1);
                        fail();
                    } catch (IllegalStateException ignored) {
                    }
                }

            }


        }
    }

    @Test
    public void redrawDice() {

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Draw the dices
            this.model.drawDice();
            int drawnDiceSize = this.model.getDiceNumberOnDraftBoard();

            // Testing normal behaviour.
            for (int j = 0; j < this.model.getDiceNumberOnDraftBoard(); j++) {

                this.model.redrawDice(j);

                assertEquals(drawnDiceSize, this.model.getDiceNumberOnDraftBoard());

            }

            // Testing against a non existing dice position.
            try {
                this.model.redrawDice(this.model.getDiceNumberOnDraftBoard());
                fail();
            } catch (IllegalArgumentException ignored) {
            }

            try {
                this.model.redrawDice(-12345);
                fail();
            } catch (IllegalArgumentException ignored) {
            }


        }
    }

    @Test
    public void redrawAllDice() {
        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Draw the dices
            this.model.drawDice();
            int drawnDiceSize = this.model.getDiceNumberOnDraftBoard();

            // Testing normal behaviour.
            this.model.redrawAllDice();
            int redrawnDiceSize = this.model.getDiceNumberOnDraftBoard();

            assertEquals(drawnDiceSize, redrawnDiceSize);

        }
    }

    @Test
    public void flipDice() {
        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Draw the dices
            this.model.drawDice();

            for (int j = 0; j < this.model.getDiceNumberOnDraftBoard(); j++) {

                DiceFace df = this.model.getDiceFaceByIndex(j);

                // Testing against an illegal position
                try {
                    this.model.flipDice(-22);
                    fail();
                } catch (IllegalArgumentException ignored) {
                }

                try {
                    this.model.flipDice(this.model.getDiceNumberOnDraftBoard());
                    fail();
                } catch (IllegalArgumentException ignored){}

                // Testing normal behaviour.
                this.model.flipDice(j);

                assertEquals(7 - df.getNumber(), this.model.getDiceFaceByIndex(this.model.getDiceNumberOnDraftBoard() - 1).getNumber());
                assertEquals(df.getColor(), this.model.getDiceFaceByIndex(this.model.getDiceNumberOnDraftBoard() - 1).getColor());

            }


        }
    }

    @Test
    public void drawDice() {

        // Can only test if throws an exception if called twice

        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Draw the dices
            this.model.drawDice();

            try {
                this.model.drawDice();
                fail();
            }catch (IllegalStateException ignored){}

        }
    }

    @Test
    public void putBackAndRedrawDice() {


        for (int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++) {

            this.views = new ArrayList<>();

            for (int j = 0; j < i; j++) {
                this.views.add(new RemoteView("Player" + j));
            }

            this.controller = new Controller(this.views, 10);
            this.model = controller.getModel();

            // Draw the dices
            this.model.drawDice();

            int diceNumber = this.model.getDiceNumberOnDraftBoard();

            for (int j = 0; j < this.model.getDiceNumberOnDraftBoard(); j++) {

                // Testing normal behaviour
                DiceFace redrawnDiceFace = this.model.putBackAndRedrawDice(j);

                assertEquals(redrawnDiceFace, this.model.getDiceFaceByIndex(this.model.getDiceNumberOnDraftBoard() - 1));

                assertEquals(diceNumber, this.model.getDiceNumberOnDraftBoard());

            }

        }

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