package it.polimi.se2018.view.CLI;

public class CLIIncrementDice extends State {
    int diceIndex;

    public CLIIncrementDice(CLIGameTable gameTable, Integer diceIndex) {
        super(gameTable);
        this.diceIndex=diceIndex;
    }

    //todo
    @Override
    public State process(String input) {
        return null;
    }

    //todo
    @Override
    public void render() {

    }
}
