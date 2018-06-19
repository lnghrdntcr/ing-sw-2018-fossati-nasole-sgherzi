package it.polimi.se2018.view.CLI;

public class CLIChooseDice extends State {

    String toolName;

    public CLIChooseDice(CLIGameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName=toolName;
    }

    @Override
    public State process(String input) {
        return null;
    }

    @Override
    public void render() {

    }
}
