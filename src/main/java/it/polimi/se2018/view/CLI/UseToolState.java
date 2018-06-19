package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class UseToolState extends State {

    private static HashMap<String, Supplier<State>> provider = new HashMap<>();


    public UseToolState(CLIGameTable gameTable) {
        super(gameTable);
    }

    private void setupProvider(){

        if(!provider.isEmpty()) return;

        provider.put("RoughingNipper", () -> {
            return new CLIChooseDice(getGameTable(),"RoughingNipper");
        });

        provider.put("EglomiseBrush", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.COLOR, "EglomiseBrush");
        });


    }

    @Override
    public State process(String input) {

        if(input.equalsIgnoreCase("cancel")) return new MainMenuState(getGameTable());
        return null;
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Select the tool card you want to use:");

        for(int i = 0; i < Settings.TOOLCARDS_N; i++){
            CLIPrinter.printToolcard(getGameTable().getToolCardImmutable(i), i);
        }

        CLIPrinter.printQuestion("[0] to [" + (Settings.TOOLCARDS_N - 1) +"] or [cancel]");

    }
}
