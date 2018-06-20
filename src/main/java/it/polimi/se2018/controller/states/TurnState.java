package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.controllerEvent.GameStartEvent;
import it.polimi.se2018.model.Tool;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.modelEvent.TurnChangedEvent;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.*;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The state that handles the state of the turn.
 *
 * @since 29/05/2018
 */
public class TurnState extends State {

    private boolean hasPlacedDice;
    private boolean hasUsedToolcard;

    private final static HashMap<String, Supplier<Boolean>> isToolCardUsable = new HashMap<>();
    private final static HashMap<String, Function<UseToolcardEvent, State>> useToolcard = new HashMap<>();

    public TurnState(Controller controller, GameTableMultiplayer model, boolean hasPlacedDice, boolean hasUsedToolcard) {
        super(controller, model);

        synchronized (isToolCardUsable) {
            if (isToolCardUsable.isEmpty()) {
                setupToolCardIsUsable();
            }
        }

        synchronized (useToolcard) {
            if (useToolcard.isEmpty()) {
                setupToolCardUse();
            }
        }


        this.hasPlacedDice = hasPlacedDice;
        this.hasUsedToolcard = hasUsedToolcard;

        if (!getController().isGameStarted()) {
            Log.d("A NEW GAME HAS STARTED!!!");
            this.getController().dispatchEvent(
                    new GameStartEvent(
                            this.getClass().getName(),
                            "",
                            ""
                    )
            );
            this.getController().setGameStarted();

        }

        this.getController().dispatchEvent(
                new TurnChangedEvent(
                        this.getClass().getName(),
                        "",
                        this.getModel().getCurrentPlayerName(),
                        this.getModel().getRound(),
                        this.getModel().isFirstTurnInRound(),
                        isDicePlaced(),
                        isToolcardUsed()
                )
        );


    }

    /**
     * Handles the incoming UseToolcardEvent.
     *
     * @param event The event to be handled.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event is null.
     */
    @Override
    public State handleToolcardEvent(UseToolcardEvent event) {
        Log.d(getClass().getCanonicalName() + " handling useToolcardEvent...");
        if (event == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");

        if (!getModel().getCurrentPlayerName().equals(event.getPlayerName()))
            Log.w("Only current player can use a toolcard");

        Tool tool = getModel().getToolCardByPosition(event.getToolCardIndex());
        int playerToken = getModel().getPlayerToken(event.getPlayerName());

        if (!isToolCardUsable.get(tool.getName()).get()) Log.i(tool.getClass().getName() + "not usable in this turn.");

        if (playerToken < tool.getNeededTokens()) {
            Log.i(
                    event.getPlayerName()
                            + " cannot use the " + tool.getClass().getName() + " toolcard:\n "
                            + "Tokens needed:\t" + tool.getNeededTokens()
                            + "\n Actual tokens:\t" + playerToken
            );
            return this;
        } else {
            return useToolcard.get(tool.getName()).apply(event);
        }

    }

    /**
     * Handles the incoming PlaceDiceEvent.
     *
     * @param event The event to be handled.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event or model is null.
     */
    @Override
    public State handlePlaceDiceEvent(PlaceDiceEvent event) {
        Log.d(getClass().getCanonicalName() + " handling PlaceDiceEvent...");
        if (event == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if (getModel() == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null");

        if (!getModel().getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + ": Only the current player can place a dice!");
            return this;
        }

        if (this.hasPlacedDice) {
            Log.w(event.getPlayerName() + " has already placed a dice.");
            return this;
        }

        if (getModel().isDiceAllowed(
                event.getPlayerName(),
                event.getPoint(),
                getModel().getDiceFaceByIndex(
                        event.getDiceFaceIndex()
                ),
                SchemaCardFace.Ignore.NOTHING)) {
            getModel().placeDice(event.getPlayerName(), event.getDiceFaceIndex(), event.getPoint());
        } else {
            return this;
        }

        return new TurnState(this.getController(), getModel(), true, this.hasUsedToolcard);

    }

    @Override
    public State handleUserTimeOutEvent() {
        return this.handleEndTurnEvent(new EndTurnEvent("UserTimeout", "", this.getModel().getCurrentPlayerName()));
    }

    /**
     * Handles the incoming EndTurnEvent.
     *
     * @param event The event to be handled.
     * @return A new Turn state.
     * @throws IllegalArgumentException If event or model is null.
     */
    @Override
    public State handleEndTurnEvent(EndTurnEvent event) {
        Log.d(getClass().getCanonicalName() + " handling EndTurnEvent...");
        if (event == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if (getModel() == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Model cannot be null");

        if (!getModel().getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + "Only the current player can end its turn!");
        } else {
            if (getModel().hasNextTurn()) {
                int oldTurn=getModel().getRound();
                getModel().nextTurn();
                if(oldTurn!=getModel().getRound()){
                    getModel().endTurn();
                }
                return new TurnState(this.getController(), getModel(), false, false);
            } else {
                return new GameEndState(this.getController(), getModel());
            }

        }

        return this;

    }

    /**
     * Check if the current player has already placed a dice
     *
     * @return true if it has, false otherwise
     */
    public boolean isDicePlaced() {
        return this.hasPlacedDice;
    }

    /**
     * Checks if the current player has already used a toolcard
     *
     * @return true if it has, false otherwise
     */
    public boolean isToolcardUsed() {
        return this.hasUsedToolcard;
    }


    /**
     * Populates the isToolCardUsable map, useful to check if the specified toolcard is usable at a certain moment
     */
    private void setupToolCardIsUsable() {
        isToolCardUsable.put("CircularCutter", () -> true);
        isToolCardUsable.put("CopperReamer", () -> true);
        isToolCardUsable.put("CorkRow", () -> true);
        isToolCardUsable.put("DiamondPad", () -> true);
        isToolCardUsable.put("EglomiseBrush", () -> true);
        isToolCardUsable.put("FirmPastaDiluent", () -> true);
        isToolCardUsable.put("FirmPastaBrush", () -> true);
        isToolCardUsable.put("Gavel", () -> !getModel().isFirstTurnInRound() && !isDicePlaced());
        isToolCardUsable.put("Lathekin", () -> true);
        isToolCardUsable.put("ManualCutter", () -> true);
        isToolCardUsable.put("RoughingNipper", () -> true);
        isToolCardUsable.put("WheeledPincer", (() -> getModel().isFirstTurnInRound()));
    }

    /**
     * Populates the useToolcard map, that contains the right helper method to use in order to activate the toolcard effect
     */
    private void setupToolCardUse() {
        useToolcard.put("CircularCutter", this::useCircularCutter);
        useToolcard.put("CopperReamer", (event) -> useMoveDice(event, SchemaCardFace.Ignore.NUMBER));
        useToolcard.put("CorkRow", this::useCorkRow);
        useToolcard.put("DiamondPad", this::useDiamondPad);
        useToolcard.put("EglomiseBrush", (event) -> useMoveDice(event, SchemaCardFace.Ignore.COLOR));
        useToolcard.put("FirmPastaDiluent", this::useFirmPastaDiluent);
        useToolcard.put("FirmPastaBrush", this::useFirmPastaBrush);
        useToolcard.put("Gavel", this::useGavel);
        useToolcard.put("Lathekin", this::useMoveDiceTwice);
        useToolcard.put("ManualCutter", this::useManualCutter);
        useToolcard.put("RoughingNipper", this::useRoughingNipper);
        useToolcard.put("WheeledPincer", this::useWheeledPincher);
    }

    /**
     * Helper method to use RoughingNipper toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useRoughingNipper(UseToolcardEvent event) {
        try {
            ChangeDiceNumberEvent ev = (ChangeDiceNumberEvent) event;
            getModel().increaseDecreaseDice(ev.getDicePosition(), ev.getNumber());
            getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
            return new TurnState(getController(), getModel(), isDicePlaced(), true);
        } catch (Exception e) {
            Log.w("Unable to flip the dice: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use MoveDice toolcard (paramterized)
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useMoveDice(UseToolcardEvent event, SchemaCardFace.Ignore ignore) {
        try {
            MoveDiceEvent e = (MoveDiceEvent) event;
            String name = getModel().getCurrentPlayerName();
            Schema tempSchema = getModel().getPlayerSchemaCopy(e.getPlayerName());
            DiceFace tempDiceFace = tempSchema.removeDiceFace(e.getSource());
            if (tempSchema.isDiceAllowed(e.getDestination(), tempDiceFace, ignore)) {
                getModel().moveDice(name, e.getSource(), e.getDestination(), true);
                getModel().useTokenOnToolcard(event.getPlayerName(), e.getToolCardIndex());
                return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
            } else {
                Log.w("Destination not allowed");
                return this;
            }
        } catch (Exception e) {
            Log.w("Unable to move dice: " + e.getMessage());
            return this;
        }

    }

    /**
     * Helper method to use MoveDiceTwice toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useMoveDiceTwice(UseToolcardEvent event) {
        try {
            DoubleMoveDiceEvent ev = (DoubleMoveDiceEvent) event;
            Schema tempSchema = getModel().getPlayerSchemaCopy(ev.getPlayerName());
            DiceFace tempDice = tempSchema.removeDiceFace(ev.getSource(0));

            if (tempSchema.isDiceAllowed(ev.getDestination(0), tempDice, SchemaCardFace.Ignore.NOTHING)) {
                tempSchema.setDiceFace(ev.getDestination(0), tempDice);
                tempDice = tempSchema.removeDiceFace(ev.getSource(1));
                if (tempSchema.isDiceAllowed(ev.getDestination(1), tempDice, SchemaCardFace.Ignore.NOTHING)) {
                    getModel().moveDice(ev.getPlayerName(), ev.getSource(0), ev.getDestination(0), false);
                    getModel().moveDice(ev.getPlayerName(), ev.getSource(1), ev.getDestination(1), true);
                    getModel().useTokenOnToolcard(event.getPlayerName(), ev.getToolCardIndex());
                    return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
                } else {
                    Log.w(getClass().getCanonicalName() + ": second move not allowed!");
                    return this;
                }

            } else {
                Log.w(getClass().getCanonicalName() + ": first move no allowed!");
                return this;
            }

        } catch (Exception e) {
            Log.w("Unable to move dices: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use CircularCutter toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useCircularCutter(UseToolcardEvent event) {
        try {
            SwapDiceFaceWithTurnHolderEvent ev = (SwapDiceFaceWithTurnHolderEvent) event;
            getModel().swapDraftDiceWithHolder(ev.getDraftBoardIndex(), ev.getTurn(), ev.getIndexInTurn());
            getModel().useTokenOnToolcard(event.getPlayerName(), ev.getToolCardIndex());
            return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
        } catch (Exception e) {
            Log.w("Unable to use CircularCutter: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use FirmPastaBrush toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useFirmPastaBrush(UseToolcardEvent event) {
        try {
            DiceActionEvent ev = (DiceActionEvent) event;
            DiceFace redrawed = getModel().redrawDice(ev.getDicePosition());

            getModel().useTokenOnToolcard(event.getPlayerName(), ev.getToolCardIndex());
            if (getModel().getPlayerSchemaCopy(ev.getPlayerName()).isDiceAllowedSomewhere(redrawed, SchemaCardFace.Ignore.NOTHING)) {
                //the diceface can be placed
                return new PlaceRedrawnDiceState(getController(), getModel(), new TurnState(getController(), getModel(), isDicePlaced(), true), redrawed, ev.getPlayerName(), getModel().getDiceNumberOnDraftBoard() - 1);
            } else {
                //the diceface cannot be placed
                return new TurnState(getController(), getModel(), isDicePlaced(), true);
            }

        } catch (Exception e) {
            Log.w(getClass().getCanonicalName() + ": cannot use FirmPastaBrush: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use Gavel toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useGavel(UseToolcardEvent event) {
        try {
            getModel().redrawAllDice();
            getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
            return new TurnState(getController(), getModel(), isDicePlaced(), true);
        } catch (Exception e) {
            Log.w("Use not allowed: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use WheeledPincher toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useWheeledPincher(UseToolcardEvent event) {
        try {
            PlaceAnotherDiceEvent ev = (PlaceAnotherDiceEvent) event;

            if (getModel().isDiceAllowed(event.getPlayerName(), ev.getPoint(), getModel().getDiceFaceByIndex(ev.getDiceFaceIndex()), SchemaCardFace.Ignore.NOTHING)) {
                getModel().placeDice(event.getPlayerName(), ev.getDiceFaceIndex(), ev.getPoint());
                getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
            } else {
                Log.w("Cannot place a dice here: ");
                return this;
            }

            getModel().playerWillDropTurn(event.getPlayerName());
            return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
        } catch (Exception e) {
            Log.w("Cannot use WheeledPincer: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use CorkRow toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useCorkRow(UseToolcardEvent event) {
        try {
            PlaceAnotherDiceEvent e = (PlaceAnotherDiceEvent) event;
            String name = getModel().getCurrentPlayerName();
            if (getModel().isAloneDiceAllowed(name, e.getPoint(), getModel().getDiceFaceByIndex(e.getDiceFaceIndex()), SchemaCardFace.Ignore.NOTHING)) {

                getModel().placeDice(e.getPlayerName(), e.getDiceFaceIndex(), e.getPoint());
                getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
                return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
            } else {
                Log.w("Destination not allowed!");
                return this;
            }
        } catch (Exception e) {
            Log.w("Cannot use CorkRow: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use DiamondPad toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useDiamondPad(UseToolcardEvent event) {
        try {
            DiceActionEvent ev = (DiceActionEvent) event;
            getModel().flipDice(ev.getDicePosition());
            getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
            return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
        } catch (Exception e) {
            Log.w("Unable to flip the dice: " + e.getMessage());
            return this;
        }
    }

    /**
     * Helper method to use FirmPastaDiluent toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useFirmPastaDiluent(UseToolcardEvent event) {
        try {
            DiceActionEvent ev = (DiceActionEvent) event;
            getModel().putBackAndRedrawDice(ev.getDicePosition());
            getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
            return new PlaceRedrawnWithNumberDiceState(getController(), getModel(), new TurnState(getController(), getModel(), this.isDicePlaced(), true),
                    ev.getPlayerName(), getModel().getDiceNumberOnDraftBoard() - 1);
        } catch (Exception e) {
            Log.w("Unable to use FirmPastaDiluent: " + e.getMessage());
            return this;
        }

    }

    /**
     * Helper method to use ManualCutter toolcard
     *
     * @param event where to get event data
     * @return the new state of the game
     */
    private State useManualCutter(UseToolcardEvent event) {
        try {
            DoubleMoveOfColorDiceEvent ev = (DoubleMoveOfColorDiceEvent) event;

            if (!getModel().isColorInDiceHolder(ev.getColor())) {
                Log.w(getClass().getCanonicalName() + ": trying to move a dice of a not existent color");
                return this;
            }

            if (!ev.getColor().equals(getModel().getPlayerDiceFace(ev.getPlayerName(), ev.getSource(0)).getColor()) ||
                    !ev.getColor().equals(getModel().getPlayerDiceFace(ev.getPlayerName(), ev.getSource(1)).getColor())) {
                Log.w(getClass().getCanonicalName() + ": trying to move a dice of a wrong color");
                return this;
            }

            Schema tempSchema = getModel().getPlayerSchemaCopy(ev.getPlayerName());
            DiceFace tempDice = tempSchema.removeDiceFace(ev.getSource(0));

            if (tempSchema.isDiceAllowed(ev.getDestination(0), tempDice, SchemaCardFace.Ignore.NOTHING)) {
                tempSchema.setDiceFace(ev.getDestination(0), tempDice);
                tempDice = tempSchema.removeDiceFace(ev.getSource(1));
                if (tempSchema.isDiceAllowed(ev.getDestination(1), tempDice, SchemaCardFace.Ignore.NOTHING)) {
                    getModel().moveDice(ev.getPlayerName(), ev.getSource(0), ev.getDestination(0), false);
                    getModel().moveDice(ev.getPlayerName(), ev.getSource(1), ev.getDestination(1), true);
                    getModel().useTokenOnToolcard(event.getPlayerName(), event.getToolCardIndex());
                    return new TurnState(getController(), getModel(), this.isDicePlaced(), true);
                } else {
                    Log.w(getClass().getCanonicalName() + ": second move not allowed!");
                    return this;
                }

            } else {
                Log.w(getClass().getCanonicalName() + ": first move no allowed!");
                return this;
            }
        } catch (Exception e) {
            Log.w("Unable to use ManualCutter: " + e.getMessage());
            return this;
        }
    }

    @Override
    public String toString() {
        return "TurnState{" +
                "hasPlacedDice=" + hasPlacedDice +
                ", hasUsedToolcard=" + hasUsedToolcard +
                ", player=" + getModel().getCurrentPlayerName() +
                ", round=" + getModel().getRound() +
                ", firstTurn=" + getModel().isFirstTurnInRound() +
                '}';
    }
}
