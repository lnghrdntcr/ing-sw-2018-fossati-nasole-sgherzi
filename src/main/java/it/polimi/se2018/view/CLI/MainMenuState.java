package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.viewEvent.EndTurnEvent;

import java.util.HashMap;
import java.util.function.Supplier;

public class MainMenuState extends State {

    private static HashMap<Integer, Supplier<State>> provider = new HashMap<>();

    public MainMenuState(CLIGameTable gameTable) {
        super(gameTable);
        this.setupProvider();
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

        provider.put(6, () -> new CLIPlaceDiceState(this.getGameTable(), SchemaCardFace.Ignore.NOTHING, false, false));

        provider.put(7, () -> new UseToolState(this.getGameTable()));

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

    @Override
    public State process(String input) {

        int selection;

        Log.d("HANDLING PROCESSING OF INPUT" + input);

        try {
            selection = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            CLIPrinter.printError("The selection must be a number!");
            return this;
        }

        if (selection <= 0 || selection > 8) {
            if (getGameTable().getView().getPlayer().equals(this.getGameTable().getView().getPlayer())) {
                CLIPrinter.printError("The selection must be between 1 and 8");
            } else {
                CLIPrinter.printError("The selection must be between 1 and 5");
            }

            return this;
        }



        if (!getGameTable().getView().getPlayer().equals(this.getGameTable().getView().getPlayer()) && selection > 5) {
            CLIPrinter.printError("It's not your turn, you motherfather!");
            return this;
        }

        if (selection == 7) {
            if(getGameTable().isToolcardUsed()){
                CLIPrinter.printError("You have already used a toolcard in this turn");
                return this;
            }else{
                provider.get(7).get();
            }
        }

        if (selection == 6) {
            if(getGameTable().isDicePlaced()){
                CLIPrinter.printError("You have already placed a dice in this turn");
                return this;
            }
        }

        return provider.get(selection).get();

    }

    @Override
    public void render() {
        System.out.println();
        System.out.println("Round: " + getGameTable().getRoundNumber() + "\nTurn:" + (getGameTable().getRoundDirection()? "1" : "2" +
                " "));
        CLIPrinter.printMenuLine(1, "View Draft Board");
        CLIPrinter.printMenuLine(2, "View Players");
        CLIPrinter.printMenuLine(3, "View Round Track");
        CLIPrinter.printMenuLine(4, "View Toolcards and Public Objectives");
        CLIPrinter.printMenuLine(5, "View your Table");
        if (this.getGameTable().isMyTurn()) {
            CLIPrinter.printMenuLine(6, "Place dice", !getGameTable().isDicePlaced());
            CLIPrinter.printMenuLine(7, "Use Toolcard", !getGameTable().isToolcardUsed());
            CLIPrinter.printMenuLine(8, "End Turn");
        }
    }

}
