package it.polimi.se2018.view;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.AbstractPlaceDiceState;
import it.polimi.se2018.view.AbstractUseToolState;
import it.polimi.se2018.view.CLI.CLIChooseDice;
import it.polimi.se2018.view.CLI.CLIMainMenuState;
import it.polimi.se2018.view.CLI.CLIPrinter;
import it.polimi.se2018.view.CLI.ShowPlayerState;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.State;
import it.polimi.se2018.view.gui.GUIChooseDice;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract class AbstractMainMenuState extends State {

    private static HashMap<Integer, Supplier<State>> provider = new HashMap<>();

    protected AbstractMainMenuState(GameTable gameTable) {
        super(gameTable);
        this.setupProvider();
    }

    public static AbstractMainMenuState createFromContext(GameTable gameTable){
        if(gameTable.getView().getGraphics()==RemoteView.Graphics.GUI){
            //TODO: CHANGE THIS
            return new CLIMainMenuState(gameTable);
        }else{
            return new CLIMainMenuState(gameTable);
        }
    }

    private void setupProvider() {

        if (!provider.isEmpty()) return;

        provider.put(1, () -> {
            CLIPrinter.printDraftBoard(this.getGameTable().getDraftBoardImmutable());
            return this;
        });

        provider.put(2, () -> new ShowPlayerState(this.getGameTable()));

        provider.put(3, () -> {
            CLIPrinter.printDiceHolder(this.getGameTable().getDiceHolderImmutable());
            return this;
        });

        provider.put(4, () -> {

            for (int i = 0; i < Settings.TOOLCARDS_N; i++) {
                CLIPrinter.printToolcard(this.getGameTable().getToolCardImmutable(i), i);
            }
            for (int i = 0; i < Settings.POBJECTIVES_N; i++) {
                CLIPrinter.printPublicObjectives(this.getGameTable().getPublicObjective(i), i);
            }

            return this;
        });

        provider.put(5, () -> {

            CLIPrinter.printSchema(
                    this.getGameTable().getSchema(
                            this.getGameTable().getView().getPlayer()
                    )
            );

            CLIPrinter.printPrivateObjective(
                    this.getGameTable().getPlayer(
                            this.getGameTable().getView().getPlayer()
                    ).getPrivateObjective()
            );

            CLIPrinter.printTokens(
                    this.getGameTable().getPlayer(
                            this.getGameTable().getView().getPlayer()
                    ).getToken(),
                    this.getGameTable().getSchema(
                            this.getGameTable().getView().getPlayer()
                    ).getSchemaCardFace().getDifficulty()
            );

            return this;
        });

        provider.put(6, () -> AbstractPlaceDiceState.createFromContext(this.getGameTable(), SchemaCardFace.Ignore.NOTHING, false, false));

        provider.put(7, () -> AbstractUseToolState.createFromContext(this.getGameTable()));

        provider.put(8, () -> {
            this.getGameTable().getView().sendEventToController(
                    new EndTurnEvent(
                            this.getClass().getName(),
                            this.getGameTable().getView().getPlayer(),
                            this.getGameTable().getView().getPlayer()
                    )
            );
            return this;
        });

    }

    public void processSelection(int selection) {
        if (selection <= 0 || selection > 8) {
            if (getGameTable().getView().getPlayer().equals(this.getGameTable().getCurrentPlayer())) {
                throw new InputError("The selection must be between 1 and 8");
            } else {
                throw new InputError("The selection must be between 1 and 5");
            }
        }


        if (!getGameTable().getView().getPlayer().equals(this.getGameTable().getCurrentPlayer()) && selection > 5) {
            throw new InputError("It's not your turn, you motherfather!");
        }

        if (selection == 7) {
            if (getGameTable().isToolcardUsed()) {
                throw new InputError("You have already used a toolcard in this turn");
            } else {
                provider.get(7).get();
            }
        }

        if (selection == 6) {
            if (getGameTable().isDicePlaced()) {
                throw new InputError("You have already placed a dice in this turn");
            }
        }

        getGameTable().setState(provider.get(selection).get());
    }


}
