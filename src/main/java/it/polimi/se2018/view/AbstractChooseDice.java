package it.polimi.se2018.view;

import it.polimi.se2018.view.CLI.*;
import it.polimi.se2018.view.gui.GUIChooseDice;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

import java.util.HashMap;
import java.util.function.Function;

/**
 * A state that ask to the player to choose a dice from the Draftboard
 */
public abstract class AbstractChooseDice extends State {

    private static HashMap<String, Function<Integer, State>> provider = new HashMap<>();

    private String toolName;

    protected AbstractChooseDice(GameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName = toolName;
        this.setupProvider();
    }

    public static AbstractChooseDice createFromContext(GameTable gameTable, String toolName){
        if(gameTable.getView().getGraphics()==RemoteView.Graphics.GUI){
            return new GUIChooseDice(gameTable, toolName);
        }else{
            return new CLIChooseDice(gameTable, toolName);
        }
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("RoughingNipper", (i) -> AbstractIncrementDice.createFromContext(getGameTable(), i));

        //5
        provider.put("CircularCutter", (i) -> AbstractExchangeWDiceHolder.createFromContext(getGameTable(), i));

        //6
        provider.put("FirmPastaBrush", (i) -> {
            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("FirmPastaBrush"), i));
            return AbstractMainMenuState.createFromContext(getGameTable());
        });

        //10
        provider.put("DiamondPad", (i) -> {
            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("DiamondPad"), i));
            return AbstractMainMenuState.createFromContext(getGameTable());
        });

        //11
        provider.put("FirmPastaDiluent", (i) -> {
            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("FirmPastaDiluent"), i));
            return AbstractMainMenuState.createFromContext(getGameTable());
        });

    }


    /**
     * Process and checks the choice of the user and goes in a new state if necessary
     * @param diceFace the dice face selected by the user
     */
    protected void processDice(int diceFace) {
        if (diceFace < 0 || diceFace >= this.getGameTable().getDraftBoardImmutable().getDices().length) {
            throw new InputError("Input out of range.");
        }
        getGameTable().setState( provider.get(this.toolName).apply(diceFace));
    }

    public void processCancel(){
        getGameTable().setState(AbstractMainMenuState.createFromContext(this.getGameTable()));
    }

}
