package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.view.AbstractExchangeWDiceHolder;
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

import java.awt.*;
import java.io.IOException;

public class GUIExchangeWDiceHolder extends AbstractExchangeWDiceHolder implements DiceHolderView.OnTurnHolderInteractionListener {


    @FXML
    private VBox root;

    private DiceHolderView roundTrack;

    @FXML
    private HBox exchangeDice;
    private Button[] buttons;

    public GUIExchangeWDiceHolder(GameTable gameTable, Integer diceIndex) {
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

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/ExchangeWDiceHolder.fxml"));

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


            exchangeDice = (HBox) scene.lookup("#chooseDice");

            roundTrack = new DiceHolderView(this);

            roundTrack.setDiceHolder(getGameTable().getDiceHolderImmutable());

            exchangeDice.getChildren().add(roundTrack);

            root.setStyle("-fx-background-image: url('/gui/background.jpg');\n" +
                    "    -fx-background-repeat: repeat;");

        });
    }

    @Override
    public void onDiceSelected(Point point) {
        try {
            processVictim(point);
        } catch (InputError ie) {
            GUIUtils.showError(ie.getMessage());
        }
    }
}
