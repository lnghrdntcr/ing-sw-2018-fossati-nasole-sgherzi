package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.CLIPlaceDice;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class UseToolState extends State {

    private static HashMap<String, Supplier<State>> provider = new HashMap<>();


    public UseToolState(CLIGameTable gameTable) {
        super(gameTable);
        this.setupProvider();
    }

    private void setupProvider(){

        if(!provider.isEmpty()) return;

        provider.put("RoughingNipper", () -> {
            return new CLIChooseDice(getGameTable(),"RoughingNipper");
        });

        provider.put("EglomiseBrush", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.COLOR, "EglomiseBrush", CLIMoveDice.Times.SECOND);
        });

        provider.put("CopperReamer", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NUMBER, "CopperReamer", CLIMoveDice.Times.SECOND);
        });

        provider.put("Lathekin", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NOTHING, "Lathekin", CLIMoveDice.Times.FIRST);
        });

        provider.put("CircularCutter", () -> {
            return new CLIChooseDice(getGameTable(),"CircularCutter");
        });

        provider.put("FirmPastaBrush", () -> {
            return new CLIChooseDice(getGameTable(),"FirmPastaBrush");
        });

        provider.put("Gavel", () -> {
            if(this.getGameTable().getToolIndexByName("Gavel") == -1) return new MainMenuState(getGameTable());

            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", this.getGameTable().getCurrentPlayer(), this.getGameTable().getToolIndexByName("Gavel"), -1));
            return new MainMenuState(getGameTable());
        });

        provider.put("WheeledPincer", () -> {
           return new CLIChooseDice(getGameTable(), "WheeledPincer");
        });

        provider.put("CorkRow", () -> {
            return new CLIChooseDice(getGameTable(),"CorkRow");
        });

        provider.put("DiamondPad", () -> {
            return new CLIChooseDice(getGameTable(),"DiamondPad");
        });

        provider.put("FirmPastaDiluent", () -> {
            return new CLIChooseDice(getGameTable(),"FirmPastaDiluent");
        });

        //todo tool 12 "ManualCutter"
    }

    @Override
    public State process(String input) {

        if(input.equalsIgnoreCase("cancel")) return new MainMenuState(getGameTable());
        return provider.get(
            this.getGameTable()
                .getToolCardImmutable(
                    Integer.parseInt(input)).getName()
        ).get();
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
