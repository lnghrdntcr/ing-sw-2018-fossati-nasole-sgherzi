package it.polimi.se2018.model;

import it.polimi.se2018.model.modelEvent.*;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Observable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * The main model entry point for multiplayer
 */
public class GameTableMultiplayer extends Observable<Event> {

    final private PublicObjective[] publicObjectives;
    final private Player[] players;
    final private Tool[] toolCards;
    final private DraftBoard draftBoard;
    final private DiceHolder diceHolder;
    final private TurnHolder turnHolder;

    public GameTableMultiplayer(PublicObjective[] publicObjectives, String[] players, Tool[] toolCards) {
        this.publicObjectives = publicObjectives;

        ArrayList<Player> playersList = new ArrayList<>();
        Arrays.stream(players).forEach((playerName) -> {
            playersList.add(new Player(playerName));
        });
        this.players = playersList.toArray(new Player[0]);


        turnHolder = new TurnHolder(players.length);
        this.toolCards = toolCards;
        draftBoard = new DraftBoard();
        diceHolder = new DiceHolder();
    }


    /**
     * @return who's playing
     */
    private Player getCurrentPlayer() {
        return players[turnHolder.getCurrentPlayer()];
    }

    /**
     * @return the name of who's playing
     */
    public String getCurrentPlayerName() {
        return getCurrentPlayer().getName();
    }

    /**
     * Return a player by its name
     *
     * @param name the name of the player
     * @return the player with the given name
     * @throws NoSuchElementException if no player with the given name exists
     */
    private Player getPlayerByName(String name) {
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * Returns the Tool card by position
     *
     * @param position zero based position of the card. Position is grater than or equal to zero and lesser than 3
     * @return the Tool card
     * @throws IllegalArgumentException if position outside of range
     */
    public Tool getToolCardByPosition(int position) {
        if (position < 0 || position >= toolCards.length)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": position must be >=0 and <3, given " + position);
        return toolCards[position];
    }

    /**
     * Returns the PublicObjectiveCard by position
     *
     * @param position zero based position of the card. Position is grater than or equal to zero and lesser than 3
     * @return the PublicObjectiveCard
     * @throws IllegalArgumentException if position outside of range
     */
    public PublicObjective getPublicObjectiveCardByPosition(int position) {
        if (position < 0 || position >= toolCards.length)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": position must be >=0 and <3, given " + position);
        return publicObjectives[position];
    }

    //Player stuff


    /**
     * Get the number of token of the player
     *
     * @param playerName the player's name
     * @return the number of token of player
     * @throws NoSuchElementException if player does not exist
     */
    public int getPlayerToken(String playerName) {
        return getPlayerByName(playerName).getToken();
    }

    /**
     * Perform the action to place a dice, without checking any condition
     *
     * @param playerName the players that perform the action
     * @param diceIndex  index of the dice in diceholder
     * @param point      where to put the dice in the Schema
     * @throws IllegalStateException if the cell is already taken
     */
    public void placeDice(String playerName, int diceIndex, Point point) {
        Player p = getPlayerByName(playerName);
        DiceFace df = draftBoard.removeDice(diceIndex);
        p.getSchema().setDiceFace(point, df);
        dispatchEvent(new SchemaChangedEvent("placeDice", playerName, p.getSchema().clone()));
        dispatchEvent(new DraftBoardChagedEvent("placeDice", null, draftBoard.getImmutableInstance()));
    }


    // Tool card stuff

    /**
     * Performs the transaction of token to use a toolcard
     *
     * @param player   the player who uses the toolcard
     * @param toolCard the toolcard used
     */
    public void useTokenOnToolcard(String player, Tool toolCard) {
        Player p = getPlayerByName(player);
        int neededToken = toolCard.getNeededTokens();
        p.setToken(p.getToken() - neededToken);
        toolCard.addToken(neededToken);
        dispatchEvent(new ToolCardChanged("useTokenOnToolcard", null, toolCard.getImmutableInstance()));
        dispatchEvent(new PlayerChangedEvent("useTokenOnToolcard", player, p.getImmutableInstance()));
    }

    /**
     * Increases or decreases a dice value
     *
     * @param diceIndex the index of the dice in the DraftBoard
     * @param direction 1 to increase, -1 to decrease
     * @throws IllegalArgumentException if diceIndex is not equal to -1 and direction is not equal to -1
     * @throws IllegalStateException    if increasing a 6 dice or decreasing a 1 dice
     */
    public void increaseDecreaseDice(int diceIndex, int direction) {
        if (direction != 1 && direction != -1)
            throw new IllegalArgumentException(getClass().getCanonicalName() + ": direction must be +1 or -1");

        DiceFace df = draftBoard.getDices()[diceIndex];
        if (direction == 1 && df.getNumber() == 6) {
            throw new IllegalStateException(getClass().getCanonicalName() + ": cannot increase a 6 dice");
        }

        if (direction == -1 && df.getNumber() == 1) {
            throw new IllegalStateException(getClass().getCanonicalName() + ": cannot decrease a 1 dice");
        }

        draftBoard.removeDice(diceIndex);
        draftBoard.addDice(new DiceFace(df.getColor(), df.getNumber() + direction));

        dispatchEvent(new DraftBoardChagedEvent("increaseDecreaseDice", null, draftBoard.getImmutableInstance()));
    }

    /**
     * Swap a DiceFace from the DraftBoard with a DiceFace from the DiceHolder area
     *
     * @param draftIndex    index of the dice in the DraftBoard
     * @param turn          the turn from wich swap the dice
     * @param turnDiceIndex the index in the turn of the dice to swipe
     */
    public void swapDraftDiceWithHolder(int draftIndex, int turn, int turnDiceIndex) {
        DiceFace oldDfDraft = draftBoard.removeDice(draftIndex);
        DiceFace oldDfHolder = diceHolder.removeDice(turn, turnDiceIndex);

        draftBoard.addDice(oldDfHolder);
        diceHolder.addDice(turn, oldDfDraft);
        dispatchEvent(new DraftBoardChagedEvent("swapDraftDiceWithHolder", null, draftBoard.getImmutableInstance()));
        dispatchEvent(new DiceHolderChangedEvent("swapDraftDiceWithHolder", null, diceHolder.getImmutableInstance()));
    }

    /**
     * Redraw a dice (change its number but not its color)
     *
     * @param index the index of the dice to redraw
     * @return the DiceFace just redrawn
     */
    public DiceFace redrawDice(int index) {
        return redrawDice(index, true);
    }

    private DiceFace redrawDice(int index, boolean singal) {
        DiceFace df = draftBoard.removeDice(index);
        df = new DiceFace(df.getColor(), new Random().nextInt(6) + 1);
        draftBoard.addDice(df);
        if (singal) dispatchEvent(new DraftBoardChagedEvent("redrawDice", null, draftBoard.getImmutableInstance()));
        return df;
    }

    /**
     * Redraw all dices (change their number but not their color)
     */
    public void redrawAllDice() {
        for (int i = 0; i < draftBoard.getDiceNumber(); i++) {
            redrawDice(0, false);
        }

        dispatchEvent(new DraftBoardChagedEvent("redrawAllDice", null, draftBoard.getImmutableInstance()));
    }

    /**
     * Flip a dice: 6 becomes 1, 5 becomes 2, 4 becomes 3 etc.
     *
     * @param index the index of the dice to flip in the DraftBoard
     */
    public void flipDice(int index) {
        DiceFace df = draftBoard.removeDice(index);
        df = new DiceFace(df.getColor(), 7 - df.getNumber());
        draftBoard.addDice(df);
        dispatchEvent(new DraftBoardChagedEvent("flipDice", null, draftBoard.getImmutableInstance()));
    }

    /**
     * Put a single dice back in the DiceBag and then redraws a new dice
     *
     * @param index index of the dice to put back in the DiceBag
     * @return the dice just drawn
     */
    public DiceFace putBackAndRedrawDice(int index) {
        draftBoard.putBackDice(index);
        draftBoard.drawSingleDice();
        DiceFace df = draftBoard.getDiceFace(draftBoard.getDiceNumber() - 1);
        dispatchEvent(new DraftBoardChagedEvent("putBackAndRedrawDice", null, draftBoard.getImmutableInstance()));
        return df;
    }

    /**
     * Change the number of a dice to a specific number
     *
     * @param index  index of the dice in the DraftBoard to change
     * @param number new value to assign to the dice. Must be between 1 and 6.
     */
    public void changeDiceNumber(int index, int number) {
        draftBoard.addDice(new DiceFace(draftBoard.removeDice(index).getColor(), number));
        dispatchEvent(new DraftBoardChagedEvent("changeDiceNumber", null, draftBoard.getImmutableInstance()));
    }

    /**
     * Move a dice in the schema of a player from position source to destination
     *
     * @param player      the player to modify
     * @param source      the source position where to pick up the dice
     * @param destination the destination where to put the dice
     * @param lastMove    if this is the last move of the set
     */
    public void moveDice(String player, Point source, Point destination, boolean lastMove) {
        Player p = getPlayerByName(player);
        p.getSchema().setDiceFace(destination, p.getSchema().removeDiceFace(source));

        if (lastMove) dispatchEvent(new SchemaChangedEvent("moveDice", player, p.getSchema().clone()));
    }

    //Turn stuff

    /**
     * End the current turn
     */
    public void nextTurn() {
        turnHolder.nextTurn();
        dispatchEvent(new TurnChangedEvent("nextTurn", players[turnHolder.getCurrentPlayer()].getName()
                , turnHolder.getRound()));
    }


    private void dispatchEvent(Event event) {
        //TODO: Do this maderfader.
        throw new NotImplementedException();
    }

    public boolean isDiceAllowed(String playerName, Point point, DiceFace diceFace, SchemaCardFace.Ignore ignore) {
        return getPlayerByName(playerName).getSchema().isDiceAllowed(point, diceFace, ignore);
    }

    public DiceFace getDiceFaceByIndex(int i) {
        return draftBoard.getDiceFace(i);
    }

    private void setPlayerSchema(String playerName, SchemaCardFace schemaCardFace) {
        if (getPlayerByName(playerName).getSchema() == null) {
            getPlayerByName(playerName).setSchema(new Schema(schemaCardFace));
        }
        else {
            throw new IllegalStateException(this.getClass().getCanonicalName() +
                ": schema alredy setted. Cannot set a new schema.");
        }
    }


}
