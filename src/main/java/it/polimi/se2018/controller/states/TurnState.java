package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
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

    public TurnState(Controller controller, boolean hasPlacedDice, boolean hasUsedToolcard) {
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

        return this;

    }

    private State handleToolcardUse(UseToolcardEvent event, GameTableMultiplayer model) {
        //TODO!
        return null;
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

        return new TurnState(this.getController(), true, this.hasUsedToolcard);

    }

    private State handleTurnEnding(EndTurnEvent event, GameTableMultiplayer model) {

        if (!model.getCurrentPlayerName().equals(event.getPlayerName())) {
            Log.w(event.getPlayerName() + "Only the current player can end its turn!");
        } else {
            model.nextTurn();
        }

        return new TurnState(this.getController(), false, false);

    }

}
