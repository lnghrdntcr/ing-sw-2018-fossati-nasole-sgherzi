package it.polimi.se2018.view;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;

import it.polimi.se2018.view.CLI.*;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class UseToolState extends State {

    private static HashMap<String, Supplier<State>> provider = new HashMap<>();


    public UseToolState(GameTable gameTable) {
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
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.COLOR, "EglomiseBrush", MoveDice.Times.SECOND);
        });

        //3
        provider.put("CopperReamer", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NUMBER, "CopperReamer", MoveDice.Times.SECOND);
        });

        //4
        provider.put("Lathekin", () -> {
            return new CLIMoveDice(getGameTable(), SchemaCardFace.Ignore.NOTHING, "Lathekin", MoveDice.Times.FIRST);
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
            if (this.getGameTable().getToolIndexByName("Gavel") == -1) return new CLIMainMenuState(getGameTable());

            if (getGameTable().getRoundDirection() || getGameTable().isDicePlaced())

                this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("Gavel"), -1));
            return new CLIMainMenuState(getGameTable());
        });

        //8
        provider.put("WheeledPincer", () -> {
            if (!getGameTable().getRoundDirection()) {
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

    public void processCancel() {
        getGameTable().setState(new CLIMainMenuState(getGameTable()));
    }

    public void processUseToolCard(int selection) {

        if (selection >= 0 && selection < Settings.TOOLCARDS_N) {

            if (getGameTable().getPlayer(getGameTable().getView().getPlayer()).getToken() < getGameTable().getToolCardImmutable(selection).getNeededTokens()) {
                throw new InputError("You don't have enough tokens! :(");
            }
            getGameTable().setState(provider.get(
                this.getGameTable()
                    .getToolCardImmutable(
                        selection).getName()
            ).get());

        }

    }

}
