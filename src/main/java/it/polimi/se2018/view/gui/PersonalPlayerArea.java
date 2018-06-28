package it.polimi.se2018.view.gui;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.RemoteView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PersonalPlayerArea extends HBox implements EventHandler<ActionEvent> {

    private final GameTable gameTable;

    private ObjectiveView privateObjectiveView;

    private SchemaPanel schemaView;

    private String player;

    @FXML
    private HBox personalPlayerArea;

    @FXML
    private VBox schema;

    @FXML
    private VBox privateObjective;

    @FXML
    private VBox interactionPanel;

    @FXML
    private Label token;

    @FXML
    private Button placeDice;

    @FXML
    private Button useToolCard;

    @FXML
    private Button endTurn;

    @FXML
    private VBox actionPanel;

    public PersonalPlayerArea(GameTable gameTable, String player) {
        this.gameTable = gameTable;
        this.player = player;
    }

    public void renderPlayer(PlayerImmutable player) {

        if (player == null || !player.getName().equals(this.player)) return;

        Platform.runLater(() -> {
            //The schema sould not be here
            this.schemaView.updateSchema(this.gameTable.getSchema(player.getName()));
            this.token.setText("Token: " + player.getToken());
            this.privateObjectiveView.setObjective(player.getPrivateObjective());

        });

    }

    public void setActive() {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/PlayerPersonalArea.fxml"));

        try {
            this.personalPlayerArea = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Platform.runLater(() -> {

            Stage secondStage = new Stage();
            secondStage.setResizable(false);
            Scene scene = new Scene(this.personalPlayerArea);
            secondStage.setScene(scene);
            secondStage.show();

            this.schema = (VBox) scene.lookup("#schema");
            this.privateObjective = (VBox) scene.lookup("#privateObjective");
            this.interactionPanel = (VBox) scene.lookup("#interactionPanel");
            this.actionPanel = (VBox) scene.lookup("#actionPanel");
            this.token = (Label) scene.lookup("#token");
            this.placeDice = (Button) scene.lookup("#placeDice");
            this.useToolCard = (Button) scene.lookup("#useToolCard");
            this.endTurn = (Button) scene.lookup("#endTurn");

            this.schemaView = new SchemaPanel(null);
            this.privateObjectiveView = new ObjectiveView();

            this.schema.getChildren().add(this.schemaView);
            this.privateObjective.getChildren().add(this.privateObjectiveView);

            this.endTurn.setText("End Turn");
            this.useToolCard.setText("Use ToolCard");
            this.placeDice.setText("Place a dice");

            // Are those useless?
            /*this.interactionPanel.getChildren().add(this.token);

            this.interactionPanel.getChildren().add(this.actionPanel);*/

        });


    }

    @Override
    public void handle(ActionEvent event) {

    }
}
