package it.polimi.se2018.view.gui;

import it.polimi.se2018.utils.Log;
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
        MediaView mediaView=null;
        try {
            Media media = new Media(getClass().getClassLoader().getResource("gui/miguel.mp3").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
             mediaView = new MediaView(mediaPlayer);
        } catch (Exception e) {
            Log.e("Cannot play ambient music: "+e.getMessage());
        }


        HBox hBox = new HBox(new Label("Playing music..."));

        if(mediaView!=null){
            hBox.getChildren().add(mediaView);
        }

        Scene scene = new Scene(hBox);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.show();




    }


}
