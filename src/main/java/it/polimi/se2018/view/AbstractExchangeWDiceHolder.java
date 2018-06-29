package it.polimi.se2018.view;

import it.polimi.se2018.view.CLI.CLIExchangeWDiceHolder;
import it.polimi.se2018.view.gui.GUIExchangeWDiceHolder;
import it.polimi.se2018.view.viewEvent.SwapDiceFaceWithDiceHolderEvent;

import java.awt.*;

public abstract class AbstractExchangeWDiceHolder extends State {

    private int diceIndex;

    protected AbstractExchangeWDiceHolder(GameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex = diceIndex;
    }

    public static AbstractExchangeWDiceHolder createFromContext(GameTable gameTable, Integer diceIndex){
        if(gameTable.getView().getGraphics()==RemoteView.Graphics.GUI){
            return new GUIExchangeWDiceHolder(gameTable, diceIndex);
        }else{
            return new CLIExchangeWDiceHolder(gameTable, diceIndex);
        }
    }

    protected void processVictim(Point victim) {

        //FIXME: this is wrong it does not check input

        if (victim == null){
            throw new InputError("Invalid Input");
        }



        getGameTable().getView().sendEventToController(
            new SwapDiceFaceWithDiceHolderEvent(
                getClass().getName(), "",
                getGameTable().getCurrentPlayer(),
                getGameTable().getToolIndexByName("CircularCutter"),
                diceIndex,
                victim.y,
                victim.x
            )
        );

        getGameTable().setState(AbstractMainMenuState.createFromContext(getGameTable()));
    }

    protected void processCancel(){
        getGameTable().setState(AbstractMainMenuState.createFromContext(this.getGameTable()));
    }


}
