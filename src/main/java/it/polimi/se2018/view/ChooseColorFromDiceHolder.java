package it.polimi.se2018.view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIMoveDice;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.CLI.State;
import it.polimi.se2018.view.GameTable;

import java.util.HashMap;
import java.util.function.Function;

public abstract class ChooseColorFromDiceHolder extends State {

    private static HashMap<String, Function<GameColor, State>> provider = new HashMap<>();

    String toolName;

    public ChooseColorFromDiceHolder(GameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName = toolName;
        this.setupProvider();
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("ManualCutter", (i) -> new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NOTHING, toolName, CLIMoveDice.Times.FIRST, i));


    }

    public void processColorSelected(GameColor color){
        boolean found = false;
        for (int i = 0; i < Settings.TURNS; i++) {
            for (DiceFace el : getGameTable().getDiceHolderImmutable().getDiceFaces(i)) {
                if (el.getColor().equals(color)) {
                    found = true;
                }
            }
        }

        if (!found) {
            throw new InputError("The color is not in the Round Track");
        }

        getGameTable().setState(provider.get(toolName).apply(color));
    }




    public void processCancel(){
        getGameTable().setState(new CLIMainMenuState(getGameTable()));
    }
}
