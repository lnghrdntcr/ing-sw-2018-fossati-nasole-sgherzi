package it.polimi.se2018.view.gui;

import it.polimi.se2018.view.AbstractIncrementDice;
import it.polimi.se2018.view.GameTable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.*;
import java.io.IOException;

public class GUIIncrementDice extends AbstractIncrementDice {


    @FXML
    private VBox root;

    private DraftBoard draftBoardView;

    @FXML
    private HBox exchangeDice;

    private Button increment;
    private Button decrement;

    public GUIIncrementDice(GameTable gameTable, Integer diceIndex) {
        super(gameTable, diceIndex);
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

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/IncrementDice.fxml"));

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

            Dice diceToUpdate = new Dice();
            diceToUpdate.setDiceFace(getGameTable().getDraftBoardImmutable().getDices()[getDiceIndex()]);


            increment = new Button();
            decrement = new Button();

            increment.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                processIncrement(1);
            });

            decrement.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                processIncrement(-1);
            });
            root.setStyle("-fx-background-image: url('/gui/background.jpg');\n" +
                    "    -fx-background-repeat: repeat;");


        });
    }


}
