package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model_view.PlayerImmutable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * A class that represent a player area
 */
public class PlayerBoard extends VBox implements EventHandler<ActionEvent> {

    @FXML
    private Label playerNameToken;

    @FXML
    private HBox playerBoard;

    @FXML
    private Label token;

    @FXML
    private HBox actionArea;

    @FXML
    private Button placeDice;

    @FXML
    private Button useToolCard;

    @FXML
    private Button endTurn;


    private SchemaPanel schemaPanel;
    private ObjectiveView objectiveView;
    private OnPlayerBoardAction eventHandler;


    public PlayerBoard(OnPlayerBoardAction eventHandler) {

        if (eventHandler == null) throw new NullPointerException("eventHandler cannot be null!!!");

        this.eventHandler = eventHandler;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/PlayerBoard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.schemaPanel = new SchemaPanel(null);
        this.objectiveView = new ObjectiveView();

        playerBoard.getChildren().add(this.schemaPanel);
        playerBoard.getChildren().add(this.objectiveView);

        placeDice.setOnAction(this);
        useToolCard.setOnAction(this);
        endTurn.setOnAction(this);

    }

    /**
     * Updates the current player of this view
     * @param player the {@link PlayerImmutable} to render
     */
    public void setPlayer(PlayerImmutable player) {

        if (player == null) return;

        if (player.getPrivateObjective() == null) {
            placeDice.setVisible(false);
            useToolCard.setVisible(false);
            endTurn.setVisible(false);
        }

        playerNameToken.setText(player.getName() + "   " + "Token: " + player.getToken());
        this.objectiveView.setObjective(player.getPrivateObjective());

    }

    /**
     * Update the Schema of the player currently shown on the screen
     * @param schema the {@link Schema} to render
     */
    public void setSchema(Schema schema) {

        if (schema == null) return;

        this.schemaPanel.updateSchema(schema);
    }


    @Override
    public void handle(ActionEvent event) {

        if (event.getSource().equals(placeDice)) {
            eventHandler.onPlayerPlaceDice();
            return;
        }

        if (event.getSource().equals(useToolCard)) {
            eventHandler.onPlayerUseToolCard();
            return;
        }

        if (event.getSource().equals(endTurn)) {
            eventHandler.onPlayerEndTurn();
            return;
        }

    }

    /**
     * An interface to handle all the possible action of the player
     */
    public interface OnPlayerBoardAction {

        /**
         * Handles the click of the user on the placeDice button
         */
        void onPlayerPlaceDice();

        /**
         * Handles the click of the user on the End Turn button
         */
        void onPlayerEndTurn();

        /**
         * Handles the click of the user on the Use Tool Card button
         */
        void onPlayerUseToolCard();

    }

}

