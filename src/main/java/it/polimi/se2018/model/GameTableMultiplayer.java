package it.polimi.se2018.model;

import it.polimi.se2018.model.modelEvent.*;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Observable;
import it.polimi.se2018.utils.ScoreHolder;
import it.polimi.se2018.utils.Settings;

import java.awt.*;
import java.util.*;

/**
 * The main model entry point for multiplayer
 */
public class GameTableMultiplayer extends Observable<Event> {

    private final PublicObjective[] publicObjectives;
    private final Player[] players;
    private final Tool[] toolCards;
    private final DraftBoard draftBoard;
    private final DiceHolder diceHolder;
    private final TurnHolder turnHolder;
    private final String[] playersName;

    private ArrayList<String> dropTurnPlayers = new ArrayList<>();

    public GameTableMultiplayer(PublicObjective[] publicObjectives, String[] players, String[] toolCards) {

        ArrayList<PrivateObjective> privateObjectives = new ArrayList<>();
        playersName = players.clone();
        this.publicObjectives = publicObjectives;

        ArrayList<Player> playersList = new ArrayList<>();
        Arrays.stream(players).forEach((playerName) -> {
            playersList.add(new Player(playerName));
        });

        for (GameColor gc : GameColor.values()) {
            privateObjectives.add(new PrivateObjective(gc));
        }

        Collections.shuffle(privateObjectives);

        playersList.forEach(player -> {
            player.setPrivateObjective(privateObjectives.get(0));
            privateObjectives.remove(0);
        });

        this.players = playersList.toArray(new Player[0]);


        turnHolder = new TurnHolder(players.length);

        this.toolCards = new Tool[Settings.TOOLCARDS_N];
        for (int i = 0; i < Settings.TOOLCARDS_N; i++) {
            this.toolCards[i] = new Tool(toolCards[i]);
        }

        draftBoard = new DraftBoard();
        diceHolder = new DiceHolder();

        // The first time the dices are drawn, does it make sense to draw it in the constructor?
        draftBoard.drawDices(this.players.length);

    }

    /**
     * Resend a copy of the whole model to the specified client
     *
     * @param playerName the client to resync
     */
    public void sync(String playerName) {

        dispatchEvent(new DraftBoardChangedEvent(this.getClass().getName() + "::gameStart", playerName, "", this.draftBoard.getImmutableInstance()));

        for (int i = 0; i < Settings.POBJECTIVES_N; i++) {

            dispatchEvent(
                new PublicObjectiveEvent(
                    this.getClass().getName() + "::gameStart",
                    playerName,
                    "",
                    this.publicObjectives[i],
                    i
                )
            );

        }
        for (int i = 0; i < Settings.TOOLCARDS_N; i++) {

            dispatchEvent(
                new ToolCardChangedEvent(
                    this.getClass().getName() + "::gameStart",
                    playerName,
                    "",
                    new ToolCardImmutable(
                        this.toolCards[i].getName(),
                        this.toolCards[i].getToken())
                    , i)
            );

        }

        for (int i = 0; i < this.players.length; i++) {
            dispatchEvent(new PlayerChangedEvent(this.getClass().getName() + "::gameStart", playerName, this.players[i].getName(), this.players[i].getImmutableInstance()));
        }
        for (int i = 0; i < this.players.length; i++) {
            dispatchEvent(new SchemaChangedEvent(this.getClass().getName() + "::gameStart", playerName, this.players[i].getName(), this.players[i].getSchema()));
        }

        dispatchEvent(new DiceHolderChangedEvent(this.getClass().getName() + "::gameStart", playerName, "", diceHolder.getImmutableInstance()));
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

    /**
     * Compute the total score of each player
     * @return a List of {@link ScoreHolder} containing the players scores, one for each player
     */
    public ArrayList<ScoreHolder> computeAllScores() {

        ArrayList<ScoreHolder> scoreHolders = new ArrayList<>();

        Arrays.stream(this.players).forEach(player -> {
            scoreHolders.add(new ScoreHolder(
                player.getName(),
                player.computeScoreFromPrivateObjective(),
                this.computePublicObjectivesScore(player.getSchema()),
                player.getToken(),
                player.computeFreeSpaces(),
                this.getPlayerPosition(player.getName())));
        });

        return scoreHolders;

    }

    /**
     * Return the index of the player in the players array
     * @param playerName the name of the player
     * @return the index of the name
     */
    private int getPlayerPosition(String playerName) {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getName().equals(playerName)) return i;
        }
        throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": no player with that name!");
    }

    /**
     * Calculate the score of a schema based on the {@link PublicObjective} on the table in this match
     *
     * @param schema the schema to process
     * @return the partial score of this schema
     */
    private int computePublicObjectivesScore(Schema schema) {
        int publicObjectivesScore = 0;
        for (PublicObjective puo : this.publicObjectives) {
            publicObjectivesScore += puo.computeScore(schema);
        }
        return publicObjectivesScore;
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
        dispatchEvent(new SchemaChangedEvent("placeDice", "", playerName, p.getSchema().cloneSchema()));
        dispatchEvent(new DraftBoardChangedEvent("placeDice", "", playerName, draftBoard.getImmutableInstance()));
    }


    // Tool card stuff

    /**
     * Performs the transaction of token to use a toolcard
     *
     * @param player the player who uses the toolcard
     * @param index  the index of the toolcard used
     */
    public void useTokenOnToolcard(String player, int index) {

        Player p = getPlayerByName(player);
        Tool toolCard = this.getToolCardByPosition(index);
        int neededToken = toolCard.getNeededTokens();
        p.setToken(p.getToken() - neededToken);
        toolCard.addToken(neededToken);
        dispatchEvent(
            new ToolCardChangedEvent(
                "useTokenOnToolcard",
                "",
                player,
                toolCard.getImmutableInstance(),
                index
            )
        );
        dispatchEvent(new PlayerChangedEvent("useTokenOnToolcard", "", player, p.getImmutableInstance()));
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

        dispatchEvent(new DraftBoardChangedEvent("increaseDecreaseDice", "", "", draftBoard.getImmutableInstance()));
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
        dispatchEvent(new DraftBoardChangedEvent("swapDraftDiceWithHolder", "", "", draftBoard.getImmutableInstance()));
        dispatchEvent(new DiceHolderChangedEvent("swapDraftDiceWithHolder", "", "", diceHolder.getImmutableInstance()));
    }

    /**
     * Redraw a dice (change its number but not its color)
     *
     * @param index the index of the dice to redraw
     * @return the DiceFace just redrawn
     */
    public DiceFace redrawDice(int index) {
        if (index >= this.getDiceNumberOnDraftBoard() || index < 0)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Trying to access an illegal position => " + index);
        return redrawDice(index, true);
    }

    /**
     * Redraw a dice (change its number but not its color)
     *
     * @param index the index of the dice to redraw
     *
     * @param singal if this call should inform the clients of this action (useful in multiple actions of the same type)
     * @return the DiceFace just redrawn
     */
    private DiceFace redrawDice(int index, boolean singal) {
        DiceFace df = draftBoard.removeDice(index);
        df = new DiceFace(df.getColor(), new Random().nextInt(6) + 1);
        draftBoard.addDice(df);
        if (singal) dispatchEvent(new DraftBoardChangedEvent("redrawDice", "", "", draftBoard.getImmutableInstance()));
        return df;
    }

    /**
     * Redraw all dices (change their number but not their color)
     */
    public void redrawAllDice() {
        for (int i = 0; i < draftBoard.getDiceNumber(); i++) {
            redrawDice(0, false);
        }

        dispatchEvent(new DraftBoardChangedEvent("redrawAllDice", "", "", draftBoard.getImmutableInstance()));
    }

    /**
     * Flip a dice: 6 becomes 1, 5 becomes 2, 4 becomes 3 etc.
     *
     * @param index the index of the dice to flip in the DraftBoard
     */
    public void flipDice(int index) {

        if (index >= this.getDiceNumberOnDraftBoard() || index < 0)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Trying to access an illegal position => " + index);

        DiceFace df = draftBoard.removeDice(index);
        df = new DiceFace(df.getColor(), 7 - df.getNumber());
        draftBoard.addDice(df);
        dispatchEvent(new DraftBoardChangedEvent("flipDice", "", "", draftBoard.getImmutableInstance()));
    }

    /**
     * Draws the dice accordling to the player number
     */
    public void drawDice() {
        if (draftBoard.getDiceNumber() > 0) {
            throw new IllegalStateException("There are still dices on the Draft Board!");
        }
        draftBoard.drawDices(players.length);
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
        dispatchEvent(new DraftBoardChangedEvent("putBackAndRedrawDice", "", "", draftBoard.getImmutableInstance()));
        return df;
    }

    /**
     * Change the number of a dice to a specific number
     *
     * @param index  index of the dice in the DraftBoard to change
     * @param number new value to assign to the dice. Must be between 1 and 6.
     */
    public void changeDiceNumber(int index, int number) {

        if (index < 0 || index >= this.getDiceNumberOnDraftBoard())
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Illegal index: given " + index + " max: " + this.getDiceNumberOnDraftBoard());
        if (number < 1 || number > 6)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Illegal number: given " + number + " bounds: [1, 6]");

        draftBoard.addDice(new DiceFace(draftBoard.removeDice(index).getColor(), number));
        dispatchEvent(new DraftBoardChangedEvent("changeDiceNumber", "", "", draftBoard.getImmutableInstance()));
    }

    /**
     * Move a dice in the schema of a player from position source to destination
     *
     * @param playerName  the player to modify
     * @param source      the source position where to pick up the dice
     * @param destination the destination where to put the dice
     * @param lastMove    if this is the last move of the set, if true signals a modify to the players
     */
    public void moveDice(String playerName, Point source, Point destination, boolean lastMove) {
        Player p = getPlayerByName(playerName);
        p.getSchema().setDiceFace(destination, p.getSchema().removeDiceFace(source));

        if (lastMove) dispatchEvent(new SchemaChangedEvent("moveDice", "", playerName, p.getSchema().cloneSchema()));
    }

    //Turn stuff

    /**
     * Check if the match has an other turn after this (the game is not ended)
     * @return true if the game is not ended, false otherwise
     */
    public boolean hasNextTurn() {

        if (
            turnHolder.isGameEnded() &&
                this.getCurrentPlayerName().equals(playersName[turnHolder.getCurrentPlayer()])
            ) return false;

        return true;

    }

    /**
     * End the current turn
     */
    public void nextTurn() {
        int oldRound = turnHolder.getRound();
        turnHolder.nextTurn();
        while (dropTurnPlayers.contains(playersName[turnHolder.getCurrentPlayer()]) && !turnHolder.isGameEnded()) {
            dropTurnPlayers.remove(playersName[turnHolder.getCurrentPlayer()]);
            turnHolder.nextTurn();
        }
        if (oldRound != turnHolder.getRound()) {
            while (draftBoard.getDiceNumber() > 0) {
                diceHolder.addDice(oldRound, draftBoard.removeDice(0));
            }
        }
    }


    /**
     * Dispatch an event to the connected clients
     * @param event the event to dispatch
     */
    private void dispatchEvent(Event event) {
        notify(event);
    }

    /**
     * Checks if a player is allowed to put a dice to a specific position in his schema
     * @param playerName the name of the player that owns the Schema
     * @param point the position where to put the diceface
     * @param diceFace the diceface to check
     * @param ignore if any restriction should be relaxed
     * @return true if the dice can be place, false otherwise
     */
    public boolean isDiceAllowed(String playerName, Point point, DiceFace diceFace, SchemaCardFace.Ignore ignore) {
        return getPlayerByName(playerName).getSchema().isDiceAllowed(point, diceFace, ignore);
    }

    /**
     * @param playerName the name of the player
     * @param point      the point where the dice face should be placed
     * @param diceFace   the dice face to check
     * @param ignore     if there is some restriction to ignore
     * @return true if the dice face is allowed, false otherwise
     */
    public boolean isAloneDiceAllowed(String playerName, Point point, DiceFace diceFace, SchemaCardFace.Ignore ignore) {
        return getPlayerByName(playerName).getSchema().isDiceAllowed(point, diceFace, ignore, true);
    }

    /**
     * Get the dice on the draftboard, based on its index
     * @param i the index of the dice on the draftboard
     * @return the diceface
     */
    public DiceFace getDiceFaceByIndex(int i) {
        return draftBoard.getDiceFace(i);
    }

    /**
     * Assign the selected SchemaCardFace to the player, based on his choice
     * @param playerName the name of the player
     * @param schemaCardFace the schema card face to assign
     */
    public void setPlayerSchema(String playerName, SchemaCardFace schemaCardFace) {

        if (schemaCardFace == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Schema cannot be null.");

        try {
            this.getPlayerByName(playerName);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": No player with that name");
        }

        if (getPlayerByName(playerName).getSchema() == null) {
            getPlayerByName(playerName).setSchema(new Schema(schemaCardFace));
        } else {
            throw new IllegalStateException(this.getClass().getCanonicalName() +
                ": schema already set. Cannot set a new schema.");
        }

        dispatchEvent(new PlayerChangedEvent(getClass().getName() + "setPlayerSchema", "", playerName, getPlayerByName(playerName).getImmutableInstance()));
    }

    /**
     * Get the players names
     *
     * @return an array containing the names of the connected clients
     */
    public String[] getPlayersName() {
        return playersName.clone();
    }


    /**
     * Check if all players have selected a schemaCardFace
     *
     * @return true if all players have selected a schemaCardFace, false otherwise
     */
    public boolean allPlayersHaveSelectedSchemaCardFace() {

        for (Player p : players) {
            if (p.getSchema() == null)
                return false;
        }
        return true;
    }

    /**
     * Return the DiceFace in a particular position in a player's schema
     *
     * @param player   the player's name
     * @param position the position of the dice face
     * @return the DiceFace placed here if there is any, null otherwise
     */
    public DiceFace getPlayerDiceFace(String player, Point position) {
        return getPlayerByName(player).getSchema().getDiceFace(position);
    }

    /**
     * @return true if this is the first turn in a match, false otherwise
     */
    public boolean isFirstTurnInRound() {
        return turnHolder.isFirstTurnInRound();
    }

    /**
     * Get a mutable copy of a player's Schema
     *
     * @param playerName the player to get the Schema
     * @return the requested copy of the Schema
     */
    public Schema getPlayerSchemaCopy(String playerName) {
        if(getPlayerByName(playerName).getSchema()==null) return null;
        return getPlayerByName(playerName).getSchema().cloneSchema();
    }

    /**
     * Checks if a specific color is present in the diceholder
     * @param gameColor the color to check
     * @return true if the color is present, false otherwise
     */
    public boolean isColorInDiceHolder(GameColor gameColor) {
        return diceHolder.isColorPresent(gameColor);
    }

    /**
     * The passed player will drop the next turn
     *
     * @param player the player that will drop the turn
     */
    public void playerWillDropTurn(String player) {
        for (String pl : playersName) {
            if (pl.equals(player)) {
                dropTurnPlayers.add(player);
                return;
            }
        }

        throw new IllegalArgumentException("Player does not exist!");
    }

    /**
     * @return the number of dice on the draft board in this moment
     */
    public int getDiceNumberOnDraftBoard() {
        return draftBoard.getDiceNumber();
    }

    /**
     * @return the schema card face selected by a player
     */
    public SchemaCardFace getPlayerSchemacardFace(String playerName) {

        try {
            this.getPlayerByName(playerName);
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": " + playerName + " does not exist");
        }

        return getPlayerByName(playerName).getSchema() == null ? null : getPlayerByName(playerName).getSchema().getSchemaCardFace();
    }

    /**
     * Returns the current round.
     *
     * @return The current round
     */
    public int getRound() {
        return turnHolder.getRound();
    }

    /**
     * Signals to the model that a new game has started and resync all the clients
     */
    public void onGameStart() {
        this.sync("");
    }

    /**
     * And the whole turn, placing all the available dice on the turnHolder and redrawing an appropriate number of dices
     */
    public void endTurn() {
        while (draftBoard.getDiceNumber() > 0) {
            diceHolder.addDice(getRound() - 1, draftBoard.removeDice(0));
        }

        if(hasNextTurn())draftBoard.drawDices(this.players.length);
        dispatchEvent(new DraftBoardChangedEvent(getClass().getName() + "::endTurn", "", "", draftBoard.getImmutableInstance()));
        dispatchEvent(new DiceHolderChangedEvent(getClass().getName() + "::endTurn", "", "", diceHolder.getImmutableInstance()));
    }

    public DiceHolderImmutable getImmutableDiceHolder(){
        return diceHolder.getImmutableInstance();
    }

}
