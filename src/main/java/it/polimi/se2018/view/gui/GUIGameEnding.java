package it.polimi.se2018.view.gui;

import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.utils.LeaderBoardHolder;
import it.polimi.se2018.utils.ScoreHolder;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.RemoteView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GUIGameEnding extends GameEnding {
    @FXML
    private VBox root;

    @FXML
    private HBox leaderboard;

    @FXML
    private VBox globalStats;

    @FXML
    private VBox players;

    @FXML
    private VBox scores;

    private ArrayList<ScoreHolder> scoreHolders;

    public GUIGameEnding(RemoteView view) {
        super(view);
    }


    private void render() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/LeaderBoard.fxml"));

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

            secondStage.setOnCloseRequest((windowEvent) -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Closing game...");
                alert.setContentText("Are you sure you want to exit?");

                Optional<ButtonType> result = alert.showAndWait();

                if (((Optional) result).get() == ButtonType.OK) {
                    Platform.exit();
                    System.exit(-1);
                } else {
                    // This is the only way I found to prevent the closing of a window, LèL
                    windowEvent.consume();
                }
            });

            leaderboard = (HBox) scene.lookup("#leaderboard");
            players = (VBox) scene.lookup("#players");
            scores = (VBox) scene.lookup("#scores");
            globalStats = (VBox) scene.lookup("#globalStats");

            for (ScoreHolder sh : scoreHolders) {

                Label player = new Label();
                Label score = new Label();

                player.setText("" + sh.getPlayerName());
                score.setText("" + sh.getTotalScore());

                player.setFont(new Font(16));
                score.setFont(new Font(16));

                players.getChildren().add(player);
                scores.getChildren().add(score);

            }

            for (LeaderBoardHolder lbh: getGlobalLeaderboardUnpacked()){

                Label label = new Label();

                label.setText("Player: " + lbh.getName()
                    + " Victories: " + lbh.getScores().optInt("victories", 0)
                    + " Losses: " + lbh.getScores().optInt("losses", 0)
                    + " Total time played: " + lbh.getScores().optInt("totalTimePlayed", 0));

                globalStats.getChildren().add(label);

            }


            root.setStyle("-fx-background-image: url('/gui/fireworks.gif');\n" +
                "    -fx-background-repeat: no-repeat;");
        });
    }

    @Override
    public void handleEndGameEvent(EndGameEvent event) {
        super.handleEndGameEvent(event);
        this.scoreHolders = event.getLeaderBoard();

        this.getView().activateGameEnding();

    }

    @Override
    public void setActive() {
        if (root == null) {
            render();
        }
    }

    @Override
    public void setInactive() {

        if (root != null) {
            Platform.runLater(() -> {
                Stage stage = (Stage) root.getScene().getWindow();
                stage.close();
                root = null;
            });
        }

    }
}
