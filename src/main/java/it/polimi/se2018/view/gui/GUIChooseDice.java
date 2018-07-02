package it.polimi.se2018.view.gui;

import it.polimi.se2018.view.CLI.CLIGameTable;
import it.polimi.se2018.view.AbstractChooseDice;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIChooseDice extends AbstractChooseDice implements DraftBoard.OnDiceSelectedListener {

    @FXML
    private VBox root;

    private DraftBoard draftBoardView;

    public GUIChooseDice(GameTable gameTable, String toolName) {
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

        Platform.runLater(() ->{
            draftBoardView.setDraftBoard(getGameTable().getDraftBoardImmutable());
        });

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

            draftBoardView = new DraftBoard(this);

            draftBoardView.setDraftBoard(getGameTable().getDraftBoardImmutable());

            root.getChildren().add(draftBoardView);

            secondStage.show();

            root.setStyle("-fx-background-image: url('/gui/background.jpg');\n" +
                    "    -fx-background-repeat: repeat;");

        });
    }

    @Override
    public void onDiceSelected(int index) {
        try {
            processDice(index);
        }catch (InputError ie){
            GUIUtils.showError(ie.getMessage());
        }
    }
}
