package it.polimi.se2018.view.CLI;

public class CLIExcangeWDiceHolder extends State {

    int diceIndex;
    public CLIExcangeWDiceHolder(CLIGameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex=diceIndex;
    }

    @Override
    public State process(String input) {
        return null;
    }

    @Override
    public void render() {

    }
}
