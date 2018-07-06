package it.polimi.se2018.view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GUILogger extends Application {

    public static void go() {
        new Thread(() -> {
            launch("");
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new HBox(new Label("LOL")));
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.show();


        try {
            Media media = new Media(getClass().getClassLoader().getResource("gui/miguel.mp3").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
            MediaView mediaView = new MediaView(mediaPlayer);
        } catch (Exception ignored) {
        }

    }


}
