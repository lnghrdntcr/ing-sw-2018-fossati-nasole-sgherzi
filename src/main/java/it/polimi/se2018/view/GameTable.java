package it.polimi.se2018.view;

import it.polimi.se2018.controller.controllerEvent.*;
import it.polimi.se2018.model.modelEvent.*;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;

import java.awt.Point;
import java.util.HashMap;

public abstract class GameTable {
    private RemoteView view;
    private DiceHolderImmutable diceHolderImmutable;
    private DraftBoardImmutable draftBoardImmutable;
    private PublicObjective[] publicObjectives;
    private HashMap<String, PlayerImmutable> players = new HashMap<>();
    private HashMap<String, Schema> schemas = new HashMap<>();
    private ToolCardImmutable[] toolCardImmutables = new ToolCardImmutable[Settings.TOOLCARDS_N];
    private boolean isMyTurn;
    private String currentPlayer;
    private int roundNumber;
    private boolean roundDirection;
    private int secondsRemaining;

    public GameTable(RemoteView view) {
        this.view = view;
    }

    final protected void placeDice(int index, Point destination) {
        view.sendEventToController(new PlaceDiceEvent(getClass().getName(), view.getPlayer(), index, destination));
    }

    final protected void useToolcard(int index) {
        //TODO
    }

    final protected void endTurn() {
        view.sendEventToController(new EndTurnEvent(getClass().getName(), view.getPlayer()));
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
        players.put(event.getPlayerImmutable().getName(), event.getPlayerImmutable());
        renderPlayer(event.getPlayerImmutable().getName());
    }

    final public void handleSchemaChanged(SchemaChangedEvent event) {
        schemas.put(event.getPlayerName(), event.getSchema());
        renderSchema(event.getPlayerName());
    }

    final public void handleToolcardChanged(ToolCardChanged event) {
        toolCardImmutables[event.getIndex()] = event.getToolCardImmutable();
        renderToolcard(event.getIndex());
    }

    final public void handlePublicObjective(PublicObjectiveEvent event){
        publicObjectives[event.getIndex()] = event.getPublicObjective();
        renderPublicObjective(event.getIndex());
    }

    final public void handleTurnChanged(TurnChangedEvent event) {
        isMyTurn = event.getPlayerName().equals(view.getPlayer());
        currentPlayer = event.getPlayerName();
        roundNumber = event.getRound();
        roundDirection = event.getDirection();
        renderTurn();
    }

    // Handle Controller events.

    final public void handleAskPlaceRedrawDice(AskPlaceRedrawDiceEvent event){
        // TODO: MACHECCAZZO
    }

    final public void handleAskPlaceRedrawDiceWithNumberSelection(AskPlaceRedrawDiceWithNumberSelectionEvent event){
        // TODO: MACHECCAZZ2
    }

    final public void handleEndGame(EndGameEvent event){
        // TODO: closes everything
    }

    final public void handlePlayerTimeout(PlayerTimeoutEvent event){
        // TODO: Dismiss windows
    }

    final public void handleTimeoutCommunication(TimeoutCommunicationEvent event){
        this.secondsRemaining = event.getTimeout();
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

    public Schema getSchemas(String player) {
        return schemas.get(player);
    }

    public ToolCardImmutable getToolCardImmutable(int index) {
        return toolCardImmutables[index];
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public String getCurrentPlayer() {
        return view.getPlayer();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public boolean getRoundDirection() {
        return roundDirection;
    }

    public PublicObjective getPublicObjective(int index) {
        return publicObjectives[index];
    }

    public abstract void handleGameStart(GameStartEvent gameStartEvent);
}
