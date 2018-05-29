package it.polimi.se2018.controller.states;

import it.polimi.se2018.controller.Controller;
import it.polimi.se2018.controller.tool.Tool;
import it.polimi.se2018.model.GameTableMultiplayer;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.viewEvent.UseToolcardEvent;

public class ToolcardState extends TurnState {


    public ToolcardState(Controller controller, String playerName, boolean hasPlacedDice, boolean hasUsedToolcard) {
        super(controller, playerName, hasPlacedDice, hasUsedToolcard);
    }

    public State handleToolcardActivation(UseToolcardEvent event, GameTableMultiplayer model) {

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
        } else {
           // model.useTokenOnToolcard(event.getPlayerName(), tool);
            // TODO: add a method in the model to expose the user using the toolcard, because tool.use() needs a player as argument.
            // TODO: call tool.use()
        }

        return this;

    }


}
