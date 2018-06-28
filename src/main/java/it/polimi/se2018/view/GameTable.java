package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.*;
import it.polimi.se2018.model.modelEvent.*;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIPlaceDiceState;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameTable {
    private RemoteView view;
    private DiceHolderImmutable diceHolderImmutable;
    private DraftBoardImmutable draftBoardImmutable;
    private PublicObjective[] publicObjectives = new PublicObjective[Settings.POBJECTIVES_N];
    private HashMap<String, PlayerImmutable> players = new HashMap<>();
    private ArrayList<String> playersName = new ArrayList<>();
    private HashMap<String, Schema> schemas = new HashMap<>();
    private ToolCardImmutable[] toolCardImmutables = new ToolCardImmutable[Settings.TOOLCARDS_N];
    private boolean isMyTurn;
    private String currentPlayer;
    private int roundNumber;
    private boolean roundDirection;
    private int secondsRemaining;
    private boolean toolcardUsed, dicePlaced;

    private State realeState;

    public GameTable(RemoteView view) {
        this.view = view;
        this.realeState = new CLIMainMenuState(this);
    }

    final protected void placeDice(int index, Point destination) {
        view.sendEventToController(new PlaceDiceEvent(getClass().getName(), "", view.getPlayer(), index, destination));
    }


    final protected void endTurn() {
        view.sendEventToController(new EndTurnEvent(getClass().getName(), view.getPlayer(), ""));
    }


    //Handling model events
    final public void handleDiceHolderChanged(DiceHolderChangedEvent event) {
        diceHolderImmutable = event.getDiceHolderImmutable();
        renderDiceHolder();
    }

    final public void handleDraftBoardChanged(DraftBoardChangedEvent event) {
        draftBoardImmutable = event.getDraftBoardImmutable();
        renderDraftBoard();
    }

    final public void handlePlayerChanged(PlayerChangedEvent event) {
        if (!playersName.contains(event.getPlayerName())) {
            this.playersName.add(event.getPlayerName());
        }
        players.put(event.getPlayerImmutable().getName(), event.getPlayerImmutable());
        renderPlayer(event.getPlayerImmutable().getName());
    }

    final public int getPlayerIndex(String player) {
        return this.playersName.indexOf(player);
    }

    final public void handleSchemaChanged(SchemaChangedEvent event) {
        schemas.put(event.getPlayerName(), event.getSchema());
        renderSchema(event.getPlayerName());
    }

    final public void handleToolcardChanged(ToolCardChangedEvent event) {
        toolCardImmutables[event.getIndex()] = event.getToolCardImmutable();
        renderToolcard(event.getIndex());
    }

    final public void handlePublicObjective(PublicObjectiveEvent event) {
        publicObjectives[event.getIndex()] = event.getPublicObjective();
        renderPublicObjective(event.getIndex());
    }

    final public void handleTurnChanged(TurnChangedEvent event) {
        isMyTurn = event.getPlayerName().equals(view.getPlayer());
        currentPlayer = event.getPlayerName();
        roundNumber = event.getRound();
        roundDirection = event.getDirection();
        toolcardUsed = event.isToolcardUsed();
        dicePlaced = event.isDicePlaced();
        getView().activateGameTable();
        renderTurn();
    }

    // Handle Controller events.

    public void handleAskPlaceRedrawDice(AskPlaceRedrawDiceEvent event){
        setState(AbstractPlaceDiceState.createFromContext(this, SchemaCardFace.Ignore.NOTHING, true, false, event.getDiceIndex(), false));
    }

    public void handleAskPlaceRedrawDiceWithNumberSelection(AskPlaceRedrawDiceWithNumberSelectionEvent event) {
        setState(AbstractPlaceDiceState.createFromContext(this, SchemaCardFace.Ignore.NOTHING, true, false, event.getDiceIndex(), true));
    }



    public void handlePlayerTimeout(PlayerTimeoutEvent event) {
        setState(new CLIMainMenuState(this));
    }


    final public void handleTimeoutCommunication(TimeoutCommunicationEvent event) {
        this.secondsRemaining = event.getTimeout();
        renderTimeOut();
    }


    //updates
    protected abstract void renderDiceHolder();

    protected abstract void renderDraftBoard();

    protected abstract void renderPlayer(String player);

    protected abstract void renderSchema(String player);

    protected abstract void renderToolcard(int index);

    protected abstract void renderPublicObjective(int index);

    protected abstract void renderTurn();

    public abstract void setActive();

    public abstract void setInactive();

    public abstract void renderTimeOut();


    //getters

    public RemoteView getView() {
        return view;
    }

    public DiceHolderImmutable getDiceHolderImmutable() {
        return diceHolderImmutable;
    }

    public DraftBoardImmutable getDraftBoardImmutable() {
        return draftBoardImmutable;
    }

    public PlayerImmutable getPlayer(String player) {
        return players.get(player);
    }

    public String[] getPlayers() {
        return players.keySet().toArray(new String[0]);
    }

    public Schema getSchema(String player) {
        return schemas.get(player);
    }

    public ToolCardImmutable getToolCardImmutable(int index) {
        return toolCardImmutables[index];
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    //1 if is the first time I play in this round
    public boolean getRoundDirection() {
        return roundDirection;
    }

    public PublicObjective getPublicObjective(int index) {
        return publicObjectives[index];
    }

    public abstract void handleGameStart(GameStartEvent gameStartEvent);

    public boolean isToolcardUsed() {
        return toolcardUsed;
    }

    public boolean isDicePlaced() {
        return dicePlaced;
    }

    public int getToolIndexByName(String toolName) {
        for (int i = 0; i < Settings.TOOLCARDS_N; i++) {
            if (getToolCardImmutable(i).getName().equals(toolName)) return i;
        }
        return -1;
    }

    public void setState(State state) {
        if(state == null) throw new NullPointerException("State cannot be null!");
        if(realeState!=null){
            realeState.unrealize();
        }
        realeState=state;
        realeState.render();

    }


    public State getRealeState() {
        return realeState;
    }
}
