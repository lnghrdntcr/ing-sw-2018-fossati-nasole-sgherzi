package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.AbstractExchangeWDiceHolder;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;

import java.awt.*;

public class CLIExchangeWDiceHolder extends AbstractExchangeWDiceHolder {


    public CLIExchangeWDiceHolder(GameTable gameTable, Integer diceIndex) {
        super(gameTable, diceIndex);
    }


    @Override
    public void process(String input) {

        if (input.equalsIgnoreCase("cancel")) {
            processCancel();
            return;
        }

        Point victim = CLIPrinter.decodePosition(input, getGameTable().getDiceHolderImmutable());

        try {
            processVictim(victim);
        } catch (InputError ie) {
            CLIPrinter.printError(ie.getMessage());
            getGameTable().setState(this);
        }

    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose the dice you want to exchange:");
        CLIPrinter.printDiceHolder(getGameTable().getDiceHolderImmutable());
    }
}
