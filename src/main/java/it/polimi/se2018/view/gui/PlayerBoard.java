package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PlayerBoard extends VBox {

    @FXML
    private Label playerName;

    @FXML
    private HBox playerBoard;

    @FXML
    private Label token;

    private SchemaPanel schemaPanel;
    private ObjectiveView objectiveView;


    public PlayerBoard() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/PlayerBoard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.schemaPanel = new SchemaPanel();
        this.objectiveView = new ObjectiveView();

        playerBoard.getChildren().add(this.schemaPanel);
        playerBoard.getChildren().add(this.objectiveView);

    }

    public void setPlayer(PlayerImmutable player) {

        if (player == null) return;

        playerName.setText(player.getName());
        token.setText("Token: " + player.getToken());
        this.objectiveView.setObjective(player.getPrivateObjective());

    }

    public void setSchema(Schema schema) {

        if (schema == null) return;

        this.schemaPanel.updateSchema(schema);
    }


}

