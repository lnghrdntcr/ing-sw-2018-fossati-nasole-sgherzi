package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;

import it.polimi.se2018.view.viewEvent.DiceActionEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class UseToolState extends State {

    private static HashMap<String, Supplier<State>> provider = new HashMap<>();


    public UseToolState(CLIGameTable gameTable) {
        super(gameTable);
        this.setupProvider();
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("RoughingNipper", () -> {
            return new CLIChooseDice(getGameTable(), "RoughingNipper");
        });

        //2
        provider.put("EglomiseBrush", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.COLOR, "EglomiseBrush", CLIMoveDice.Times.SECOND);
        });

        //3
        provider.put("CopperReamer", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NUMBER, "CopperReamer", CLIMoveDice.Times.SECOND);
        });

        //4
        provider.put("Lathekin", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NOTHING, "Lathekin", CLIMoveDice.Times.FIRST);
        });

        //5
        provider.put("CircularCutter", () -> {
            return new CLIChooseDice(getGameTable(), "CircularCutter");
        });

        //6
        provider.put("FirmPastaBrush", () -> {
            return new CLIChooseDice(getGameTable(), "FirmPastaBrush");
        });

        //7
        provider.put("Gavel", () -> {
            if (this.getGameTable().getToolIndexByName("Gavel") == -1) return new MainMenuState(getGameTable());

            if(getGameTable().getRoundDirection() || getGameTable().isDicePlaced())

            this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("Gavel"), -1));
            return new MainMenuState(getGameTable());
        });

        //8
        provider.put("WheeledPincer", () -> {
            if(!getGameTable().getRoundDirection()){
                CLIPrinter.printError("You can't activate this card now!");
                return this;
            }
            return new CLIPlaceDiceState(getGameTable(), SchemaCardFace.Ignore.NOTHING, true, false);
        });

        //9
        provider.put("CorkRow", () -> {
            return new CLIPlaceDiceState(getGameTable(), SchemaCardFace.Ignore.NOTHING, true, true);
        });

        //10
        provider.put("DiamondPad", () -> {
            return new CLIChooseDice(getGameTable(), "DiamondPad");
        });

        //11
        provider.put("FirmPastaDiluent", () -> {
            return new CLIChooseDice(getGameTable(), "FirmPastaDiluent");
        });

        provider.put("ManualCutter", () -> new CLIChooseColorFromDiceHolder(getGameTable(), "ManualCutter"));
    }

    @Override
    public State process(String input) {

        if (input.equalsIgnoreCase("cancel")) return new MainMenuState(getGameTable());
        if(getGameTable().getPlayer(getGameTable().getCurrentPlayer()).getToken() < getGameTable().getToolCardImmutable(Integer.parseInt(input)).getNeededTokens()){
            CLIPrinter.printError("You don't have enough tokens! :(");
            return new MainMenuState(getGameTable());
        }
        return provider.get(
                this.getGameTable()
                        .getToolCardImmutable(
                                Integer.parseInt(input)).getName()
        ).get();
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Select the tool card you want to use:");

        for (int i = 0; i < Settings.TOOLCARDS_N; i++) {
            CLIPrinter.printToolcard(getGameTable().getToolCardImmutable(i), i);
        }

        CLIPrinter.printQuestion("[0] to [" + (Settings.TOOLCARDS_N - 1) + "] or [cancel]");

    }
}
