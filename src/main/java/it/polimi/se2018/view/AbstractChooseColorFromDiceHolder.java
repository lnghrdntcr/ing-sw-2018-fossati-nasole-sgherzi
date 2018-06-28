package it.polimi.se2018.view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIChooseColorFromDiceHolder;

import java.util.HashMap;
import java.util.function.Function;

public abstract class AbstractChooseColorFromDiceHolder extends State {

    private static HashMap<String, Function<GameColor, State>> provider = new HashMap<>();

    String toolName;

    protected AbstractChooseColorFromDiceHolder(GameTable gameTable, String toolName) {
        super(gameTable);
        this.toolName = toolName;
        this.setupProvider();
    }

    public static AbstractChooseColorFromDiceHolder createFromContext(GameTable gameTable, String toolName){
        if(gameTable.getView().getGraphics()==RemoteView.Graphics.GUI){
            //TODO: change this!
            return new CLIChooseColorFromDiceHolder(gameTable, toolName);
        }else{
            return new CLIChooseColorFromDiceHolder(gameTable, toolName);
        }
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("ManualCutter", (i) -> AbstractMoveDice.createFromContext(getGameTable(), SchemaCardFace.Ignore.NOTHING, toolName, AbstractMoveDice.Times.FIRST, i));


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
        getGameTable().setState(AbstractMainMenuState.createFromContext(getGameTable()));
    }
}
