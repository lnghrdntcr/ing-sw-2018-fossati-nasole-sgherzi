package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class MainView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/MainView.fxml"));
        VBox root = loader.load();

        SchemaPanel schemaPanel=new SchemaPanel();
        VBox.setVgrow(schemaPanel, Priority.ALWAYS);
        root.getChildren().add(schemaPanel);


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);

        primaryStage.show();


        SchemaCardFace cardFace= SchemaCard.loadSchemaCardsFromJson("./gameData/resources/schemaCards/schemaCardBase.scf").get(0).getFace(Side.FRONT);
        Log.i(cardFace.getName());
        Schema schema = new Schema(cardFace);
        schemaPanel.updateSchema(schema);
        schemaPanel.updateToken(3);
    }
}
