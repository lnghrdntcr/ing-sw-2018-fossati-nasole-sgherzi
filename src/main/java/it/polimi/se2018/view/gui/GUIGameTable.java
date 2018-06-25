package it.polimi.se2018.view.gui;

import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceEvent;
import it.polimi.se2018.controller.controllerEvent.AskPlaceRedrawDiceWithNumberSelectionEvent;
import it.polimi.se2018.controller.controllerEvent.GameStartEvent;
import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.RemoteView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class GUIGameTable extends GameTable implements EventHandler<ActionEvent> {

    @FXML
    private GridPane root;

    private ArrayList<PlayerBoard> players = new ArrayList<>();
    private ArrayList<ToolCard> toolCardsList = new ArrayList<>();
    private ArrayList<ObjectiveView> publicObjectivesList = new ArrayList<>();

    private DiceHolderView diceHolderView;

    private DraftBoard draftBoardView;

    @FXML
    private VBox player1;

    @FXML
    private VBox roundTrack;

    @FXML
    private HBox publicObjectives;

    @FXML
    private HBox draftBoard;

    // TODO: DraftBoard needs to be an element

    @FXML
    private VBox player3;

    @FXML
    private VBox player2;

    @FXML
    private HBox toolCards;

    @FXML
    private VBox player4;

    public GUIGameTable(RemoteView view) {
        super(view);
    }

    @Override
    public void handleAskPlaceRedrawDice(AskPlaceRedrawDiceEvent event) {

    }

    @Override
    public void handleAskPlaceRedrawDiceWithNumberSelection(AskPlaceRedrawDiceWithNumberSelectionEvent event) {

    }

    @Override
    public void handlePlayerTimeout(PlayerTimeoutEvent event) {

    }

    @Override
    protected void renderDiceHolder() {

        if (diceHolderView == null) return;

        this.diceHolderView.setDiceHolder(this.getDiceHolderImmutable());

    }

    @Override
    protected void renderDraftBoard() {

        if (draftBoardView == null || getDraftBoardImmutable() == null) return;

        draftBoardView.setDraftBoard(getDraftBoardImmutable());

    }

    @Override
    protected void renderPlayer(String player) {

        if (this.players.isEmpty()) return;

        PlayerImmutable playerImmutable = getPlayer(player);

        Platform.runLater(() -> {
            this.players.get(getPlayerIndex(player)).setPlayer(playerImmutable);
        });

    }

    @Override
    protected void renderSchema(String player) {

        if (this.players.isEmpty()) return;
        Schema schema = getSchema(player);

        Platform.runLater(() -> {
            this.players.get(getPlayerIndex(player)).setSchema(schema);
        });

    }

    @Override
    protected void renderToolcard(int index) {

        if (this.toolCardsList.isEmpty()) return;

        ToolCardImmutable toolcard = getToolCardImmutable(index);

        Platform.runLater(() -> {
            this.toolCardsList.get(index).setToolCard(toolcard);
        });


    }

    @Override
    protected void renderPublicObjective(int index) {

    }

    @Override
    protected void renderTurn() {

    }

    @Override
    public void setActive() {

        if (root != null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/MainView.fxml"));

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Platform.runLater(() -> {

            Stage secondStage = new Stage();
            secondStage.setResizable(false);
            Scene scene = new Scene(root);
            secondStage.setScene(scene);
            secondStage.show();

            player1 = (VBox) scene.lookup("#player1");
            player2 = (VBox) scene.lookup("#player2");
            player3 = (VBox) scene.lookup("#player3");
            player4 = (VBox) scene.lookup("#player4");

            players.add(new PlayerBoard());
            players.add(new PlayerBoard());
            players.add(new PlayerBoard());
            players.add(new PlayerBoard());

            player1.getChildren().add(players.get(0));
            player2.getChildren().add(players.get(1));
            player3.getChildren().add(players.get(2));
            player4.getChildren().add(players.get(3));

            diceHolderView = new DiceHolderView();
            draftBoardView = new DraftBoard();

            Arrays.stream(getPlayers()).forEach(player -> {
                this.players.get(getPlayerIndex(player)).setPlayer(getPlayer(player));
                this.players.get(getPlayerIndex(player)).setSchema(getSchema(player));
            });

            publicObjectives = (HBox) scene.lookup("#publicObjectives");
            toolCards = (HBox) scene.lookup("#toolCards");
            draftBoard = (HBox) scene.lookup("#draftBoard");
            roundTrack = (VBox) scene.lookup("#roundTrack");


            for (int i = 0; i < Settings.POBJECTIVES_N; i++) {
                this.publicObjectivesList.add(new ObjectiveView());
                this.publicObjectivesList.get(i).setObjective(getPublicObjective(i));
                this.publicObjectives.getChildren().add(this.publicObjectivesList.get(i));
            }

            for (int i = 0; i < Settings.TOOLCARDS_N; i++) {
                this.toolCardsList.add(new ToolCard());
                this.toolCardsList.get(i).setToolCard(getToolCardImmutable(i));
                this.toolCards.getChildren().add(this.toolCardsList.get(i));
            }

            roundTrack.getChildren().add(diceHolderView);
            draftBoard.getChildren().add(draftBoardView);

            draftBoardView.setDraftBoard(getDraftBoardImmutable());

        });

    }

    @Override
    public void setInactive() {

        if(root == null) return;

        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
            root = null;
        });
    }

    @Override
    public void renderTimeOut() {

    }

    @Override
    public void handleGameStart(GameStartEvent gameStartEvent) {

    }

    @Override
    public void handle(ActionEvent event) {

    }

}
