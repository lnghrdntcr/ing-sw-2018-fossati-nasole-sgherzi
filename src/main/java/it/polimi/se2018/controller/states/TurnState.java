package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;
import it.polimi.se2018.view.viewEvent.PlaceDiceEvent;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

public class TurnState extends State {

    private boolean hasPlacedDice;
    private boolean hasUsedToolcard;

    public TurnState(Controller controller, String playerName, boolean hasPlacedDice, boolean hasUsedToolcard) {
        super(controller);
        this.hasPlacedDice = hasPlacedDice;
        this.hasUsedToolcard = hasUsedToolcard;
    }

    @Override
    public State handleEvent(Event event, GameTableMultiplayer model) {
        if (event == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Event cannot be null");
        if (event instanceof UseToolcardEvent) return this.handleToolcardUse((UseToolcardEvent) event, model);
        if (event instanceof PlaceDiceEvent) return this.handleDicePlacing((PlaceDiceEvent) event, model);
        if (event instanceof EndTurnEvent) return this.handleTurnEnding((EndTurnEvent) event, model);

        return new TurnState(this.getController(), model.getCurrentPlayerName(), this.hasPlacedDice, this.hasUsedToolcard);

    }

    private State handleToolcardUse(UseToolcardEvent event, GameTableMultiplayer model) {
        Tool tool = model.getToolCardByPosition(event.getToolCardIndex());
        int playerToken = model.getPlayerToken(event.getPlayerName());

        if (!model.getCurrentPlayerName().equals(event.getPlayerName())) Log.w("Only current player can use a toolcard");

        if (!tool.isUsable()) Log.i(tool.getClass().getName() + "not usable in this turn.");

        if (playerToken < tool.getNeededTokens()) {
            Log.i(
                    event.getPlayerName()
                            + " cannot use the " + tool.getClass().getName() + " toolcard:\n "
                            + "Tokens needed:\t" + tool.getNeededTokens()
                            + "\n Actual tokens:\t" + playerToken
            );
            return this;
        } else {
            return tool.use(getController(), model, this, event);
        }

    }

    private State handleDicePlacing(PlaceDiceEvent event, GameTableMultiplayer model) {

        if (this.hasPlacedDice) Log.w(event.getPlayerName() + " has already placed a dice.");

        if (!model.getCurrentPlayerName().equals(event.getPlayerName()))
            Log.w(event.getPlayerName() + ": Only the current player can place a dice!");
        if (
            model.isDiceAllowed(
                event.getPlayerName(),
                event.getPoint(),
                model.getDiceFaceByIndex(
                    event.getDiceFaceIndex()
                ),
                SchemaCardFace.Ignore.NOTHING
            )
            ) model.placeDice(event.getPlayerName(), event.getDiceFaceIndex(), event.getPoint());

        return new TurnState(this.getController(), model.getCurrentPlayerName(), true, this.hasUsedToolcard);

    }

    private State handleTurnEnding(EndTurnEvent event, GameTableMultiplayer model) {

        if (!model.getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + "Only the current player can end its turn!");
        } else {
            model.nextTurn();
        }

        return new TurnState(this.getController(), model.getCurrentPlayerName(), false, false);

    }

    public boolean isDicePlaced() {
        return hasPlacedDice;
    }

}
