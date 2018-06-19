package it.polimi.se2018.view.CLI;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class CLIChooseDice extends State {

    private static HashMap<String, Function<Integer, State>> provider = new HashMap<>();

    String toolName;

    public CLIChooseDice(CLIGameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName=toolName;
        this.setupProvider();
    }

    private void setupProvider(){

        if(!provider.isEmpty()) return;

        provider.put("RoughingNipper", (i) -> new CLIIncrementDice(getGameTable(), i));

    }

    //TODO

    @Override
    public State process(String input) {
        return null;
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose a dice: [0] to [" + getGameTable().getDraftBoardImmutable().getDices().length + "]");
        CLIPrinter.printDraftBoard(getGameTable().getDraftBoardImmutable());
    }
}
