package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;

import java.util.HashMap;
import java.util.function.Function;

public class CLIChooseColorFromDiceHolder extends State {

    private static HashMap<String, Function<GameColor, State>> provider = new HashMap<>();

    String toolName;

    public CLIChooseColorFromDiceHolder(CLIGameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName = toolName;
        this.setupProvider();
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("ManualCutter", (i) -> new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NOTHING, toolName, CLIMoveDice.Times.FIRST, i));


    }

    @Override
    public void process(String input) {
        if (input.equalsIgnoreCase("cancel")) getGameTable().setState(new CLIMainMenuState(getGameTable()));

        GameColor color = null;
        for (GameColor c : GameColor.values()) {
            if (c.toString().equalsIgnoreCase(input)) {
                color = c;
            }
        }

        if (color == null) {
            CLIPrinter.printError("Invalid choice");
            getGameTable().setState(this);
        }

        boolean found = false;
        for (int i = 0; i < Settings.TURNS; i++) {
            for (DiceFace el : getGameTable().getDiceHolderImmutable().getDiceFaces(i)) {
                if (el.getColor().equals(color)) {
                    found = true;
                }
            }
        }

        if (!found) {
            CLIPrinter.printError("The color is not in the Round Track");
            getGameTable().setState(this);
        }

        getGameTable().setState(provider.get(toolName).apply(color));


    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Select a color:");
        for (GameColor c : GameColor.values()) {
            CLIPrinter.printQuestion(c.toString().toLowerCase());
        }
    }
}
