package it.polimi.se2018.model;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.controller.states.TurnState;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.model.ImmutableCloneable;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;

/**
 * A general class for a tool card, that implements it's logic
 */
public class Tool implements ImmutableCloneable<ToolCardImmutable> {



    private int token=0;
    private String name;


    public Tool(String name){
        this.name = name;
    }

    /**
     * Obtains the number of tokens used on this toolcard
     * @return the number of token
     */
    public int getToken() {
        return token;
    }

    /**
     * Add the given token to this toolcard
     * @param tokenAdded
     */
    public void addToken(int tokenAdded) { this.token += tokenAdded; }

    /**
     * Obtain the number of token needed by the player to use this toolcard.
     * @return 1 if there is no token on the card, 2 otherwise
     */
    public int getNeededTokens() {
        return token > 0 ? 2 : 1;
    }

    @Override
    public ToolCardImmutable getImmutableInstance() {
        return new ToolCardImmutable(name, token);
    }

    public String getName() {
        return name;
    }
}
