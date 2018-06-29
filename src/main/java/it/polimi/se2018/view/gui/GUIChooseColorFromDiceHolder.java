package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.view.AbstractChooseColorFromDiceHolder;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIChooseColorFromDiceHolder extends AbstractChooseColorFromDiceHolder {
    @FXML
    private VBox root;

    @FXML
    private HBox chooseColor;
    private Button[] buttons;

    public GUIChooseColorFromDiceHolder(GameTable gameTable, String toolName) {
        super(gameTable, toolName);
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

    @Override
    public void render() {
        if (root == null) {
            buildInterface();
        }
    }

    private void buildInterface() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/ChooseColorFromDiceHolder.fxml"));

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
            secondStage.show();


            chooseColor = (HBox) scene.lookup("#chooseColor");

            for (int i = 0; i < GameColor.values().length; i++) {
                buttons[i] = new Button();
                buttons[i].setText(GameColor.values()[i].toString());
                chooseColor.getChildren().add(buttons[i]);
                final int index = i;
                buttons[i].setOnAction((ev) -> {
                    try {
                        processColorSelected(GameColor.values()[index]);
                    }catch (InputError ie){
                        GUIUtils.showError(ie.getMessage());
                    }
                });
            }
        });
    }
}