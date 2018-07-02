package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.view.AbstractMoveDice;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GUIMoveDice extends AbstractMoveDice implements SchemaPanel.OnSchemaInteractionListener {
    @FXML
    private VBox root;

    private SchemaPanel schemaPanel;

    private Label questionLabel;

    public GUIMoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times) {
        super(gameTable, ignore, toolName, times);
    }

    public GUIMoveDice(GameTable gameTable, SchemaCardFace.Ignore ignore, String toolName, Times times, GameColor color) {
        super(gameTable, ignore, toolName, times, color);
    }

    @Override
    public void process(String input) {
    }

    @Override
    public void unrealize() {

        if (root != null) {
            Platform.runLater(() -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
                root = null;
            });
        }

    }

    private void buildInterface() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/ChooseDice.fxml"));

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

            secondStage.setOnCloseRequest((windowEvent) -> processCancel());

            schemaPanel = new SchemaPanel(this);

            schemaPanel.updateSchema(getTemporaryPlayerSchema());

            root.getChildren().add(schemaPanel);

            secondStage.show();

            questionLabel = (Label) scene.lookup("#question");

        });
    }

    @Override
    public void render() {

        if (root == null) {
            buildInterface();
        }

        Platform.runLater(() -> {
            schemaPanel.updateSchema(getTemporaryPlayerSchema());
            if (getActionState() == ActionState.CHOOSE) {

                questionLabel.setText("Choose the " + this.getTimes().toString().toLowerCase() + " dice to move: ");
            } else {
                questionLabel.setText("Choose the destination: ");
            }

        });

    }

    @Override
    public void onCellSelected(Point point) {
        try {
            if (getActionState() == ActionState.CHOOSE) {
                if (getTimes() == Times.FIRST) {
                    processFirstSource(point);
                } else if (getTimes() == Times.SECOND) {
                    processSecondSource(point);
                }

                return;
            } else if (getActionState() == ActionState.PLACE) {

                if (getTimes() == Times.FIRST) {
                    processFirstDestination(point);
                } else if (getTimes() == Times.SECOND) {
                    processSecondDestination(point);
                }

            }
        } catch (InputError ie) {
            GUIUtils.showError(ie.getMessage());
            return;
        }
    }
}
