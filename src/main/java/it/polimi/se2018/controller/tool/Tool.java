package it.polimi.se2018.controller.tool;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.ImmutableCloneable;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;

public abstract class Tool implements ImmutableCloneable<ToolCardImmutable> {

    private int token=0;

    public abstract boolean isUsable(GameTableMultiplayer model, TurnState currentState);

    public abstract State use(Controller controller, GameTableMultiplayer model, TurnState state, Event event);

    public int getToken() {
        return token;
    }

    public void addToken(int tokenAdded) { this.token += tokenAdded; }

    public int getNeededTokens() {
        return token > 0 ? 2 : 1;
    }
}
