package it.polimi.se2018.view;

import it.polimi.se2018.view.CLI.CLIIncrementDice;
import it.polimi.se2018.view.gui.GUIIncrementDice;
import it.polimi.se2018.view.viewEvent.ChangeDiceNumberEvent;

/**
 * A state that ask if the user wants to increment or decrement a dice
 */
public abstract class AbstractIncrementDice extends State {
    private int diceIndex;

    protected AbstractIncrementDice(GameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex = diceIndex;
    }

    public static AbstractIncrementDice createFromContext(GameTable gameTable, Integer diceIndex) {
        if (gameTable.getView().getGraphics() == RemoteView.Graphics.GUI) {
            return new GUIIncrementDice(gameTable, diceIndex);
        } else {
            return new CLIIncrementDice(gameTable, diceIndex);
        }
    }

    /**
     * Process and checks the choice of the user and goes in a new state if necessary
     * @param increment +1 for incrementation, -1 for decrementation
     */
    protected void processIncrement(int increment) {
        if (increment == -1 && getGameTable().getDraftBoardImmutable().getDices()[diceIndex].getNumber() == 1) {
            throw new InputError("Cannot decrement a 1 dice!");
        }

        if (increment == 1 && getGameTable().getDraftBoardImmutable().getDices()[diceIndex].getNumber() == 6) {
            throw new InputError("Cannot increment a 6 dice!");
        }

        this.getGameTable().getView().sendEventToController(
            new ChangeDiceNumberEvent(
                this.getClass().getName(),
                "",
                this.getGameTable().getView().getPlayer(),
                this.getGameTable().getToolIndexByName("RoughingNipper"),
                this.diceIndex,
                increment
            ));

        getGameTable().setState(AbstractMainMenuState.createFromContext(this.getGameTable()));
    }

    /**
     * Process the will of the user to cancel the current action
     */
    public void processCancel() {
        getGameTable().setState(AbstractMainMenuState.createFromContext(this.getGameTable()));
    }

    public int getDiceIndex() {
        return diceIndex;
    }
}
