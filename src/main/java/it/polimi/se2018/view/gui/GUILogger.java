package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.objectives.MediumShades;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class GUILogger extends Application {

    public static void go() {
        new Thread(()->{launch("");}).start();
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(new HBox(new Label("LOL")));
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.show();

    }



}
