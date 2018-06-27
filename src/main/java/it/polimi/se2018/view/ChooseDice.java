package it.polimi.se2018.view;

import it.polimi.se2018.view.CLI.*;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

import java.util.HashMap;
import java.util.function.Function;

public abstract class ChooseDice extends State {

    private static HashMap<String, Function<Integer, State>> provider = new HashMap<>();

    private String toolName;

    public ChooseDice(GameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName = toolName;
        this.setupProvider();
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("RoughingNipper", (i) -> new CLIIncrementDice(getGameTable(), i));

        //5
        provider.put("CircularCutter", (i) -> new CLIExchangeWDiceHolder(getGameTable(), i));

        //6
        provider.put("FirmPastaBrush", (i) -> {
            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("FirmPastaBrush"), i));
            return new CLIMainMenuState(getGameTable());
        });

        //10
        provider.put("DiamondPad", (i) -> {
            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("DiamondPad"), i));
            return new CLIMainMenuState(getGameTable());
        });

        //11
        provider.put("FirmPastaDiluent", (i) -> {
            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("FirmPastaDiluent"), i));
            return new CLIMainMenuState(getGameTable());
        });

    }


    public void processDice(int diceFace) {
        if (diceFace < 0 || diceFace >= this.getGameTable().getDraftBoardImmutable().getDices().length) {
            throw new InputError("Input out of range.");
        }
        getGameTable().setState( provider.get(this.toolName).apply(diceFace));
    }

    public void processCancel(){
        getGameTable().setState(new CLIMainMenuState(this.getGameTable()));
    }

}
