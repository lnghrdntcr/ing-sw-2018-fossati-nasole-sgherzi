package it.polimi.se2018.view;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.CLI.CLIUseToolState;
import it.polimi.se2018.view.gui.GUIUseToolState;
import it.polimi.se2018.view.viewEvent.DiceActionEvent;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * A state that asks the user to select as toolcard to use
 */
public abstract class AbstractUseToolState extends State {

    private static HashMap<String, Supplier<State>> provider = new HashMap<>();


    public AbstractUseToolState(GameTable gameTable) {
        super(gameTable);
        this.setupProvider();
    }

    public static AbstractUseToolState createFromContext(GameTable gameTable) {
        if (gameTable.getView().getGraphics() == RemoteView.Graphics.GUI) {
            return new GUIUseToolState(gameTable);
        } else {
            return new CLIUseToolState(gameTable);
        }
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        //1
        provider.put("RoughingNipper", () -> {
            return AbstractChooseDice.createFromContext(getGameTable(), "RoughingNipper");
        });

        //2
        provider.put("EglomiseBrush", () -> {
            return AbstractMoveDice.createFromContext(getGameTable(), SchemaCardFace.Ignore.COLOR, "EglomiseBrush", AbstractMoveDice.Times.SECOND);
        });

        //3
        provider.put("CopperReamer", () -> {
            return AbstractMoveDice.createFromContext(getGameTable(), SchemaCardFace.Ignore.NUMBER, "CopperReamer", AbstractMoveDice.Times.SECOND);
        });

        //4
        provider.put("Lathekin", () -> {
            return AbstractMoveDice.createFromContext(getGameTable(), SchemaCardFace.Ignore.NOTHING, "Lathekin", AbstractMoveDice.Times.FIRST);
        });

        //5
        provider.put("CircularCutter", () -> {
            return AbstractChooseDice.createFromContext(getGameTable(), "CircularCutter");
        });

        //6
        provider.put("FirmPastaBrush", () -> {
            return AbstractChooseDice.createFromContext(getGameTable(), "FirmPastaBrush");
        });

        //7
        provider.put("Gavel", () -> {

            if (!getGameTable().getRoundDirection() && !getGameTable().isDicePlaced()) {

                this.getGameTable().getView().sendEventToController(new DiceActionEvent(this.getClass().getName(),
                    "", getGameTable().getView().getPlayer(), this.getGameTable().getToolIndexByName("Gavel"), -1));
            } else {
                throw new InputError("You can't activate this card now!");
            }
            return AbstractMainMenuState.createFromContext(getGameTable());
        });

        //8
        provider.put("WheeledPincer", () -> {
            if (!getGameTable().getRoundDirection()) {
                throw new InputError("You can't activate this card now!");
            }
            return AbstractPlaceDiceState.createFromContext(getGameTable(), SchemaCardFace.Ignore.NOTHING, true, false);
        });

        //9
        provider.put("CorkRow", () -> {
            return AbstractPlaceDiceState.createFromContext(getGameTable(), SchemaCardFace.Ignore.NOTHING, true, true);
        });

        //10
        provider.put("DiamondPad", () -> {
            return AbstractChooseDice.createFromContext(getGameTable(), "DiamondPad");
        });

        //11
        provider.put("FirmPastaDiluent", () -> {
            return AbstractChooseDice.createFromContext(getGameTable(), "FirmPastaDiluent");
        });

        provider.put("ManualCutter", () -> AbstractChooseColorFromDiceHolder.createFromContext(getGameTable(), "ManualCutter"));
    }

    /**
     * Process the will of the user to cancel the current action
     */
    public void processCancel() {
        getGameTable().setState(AbstractMainMenuState.createFromContext(getGameTable()));
    }

    /**
     * Process and checks the choice of the user and goes in a new state if necessary
     * @param selection the toolcar selected by the user
     */
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

        } else {
            throw new InputError("The number is not between 0 and " + (Settings.TOOLCARDS_N - 1));
        }
    }

}
