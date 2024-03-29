package it.polimi.se2018.view.gui;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.controller.controllerEvent.LogEvent;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.SelectSchemaCardFace;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class GUISelectSchemaCardFace extends SelectSchemaCardFace implements EventHandler<ActionEvent> {

    private GridPane root;

    @FXML
    private ScrollPane logsPanel;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonSchema1;

    @FXML
    private Button buttonSchema2;

    @FXML
    private VBox box3;

    @FXML
    private VBox playerPrivateObjectiveBox;

    @FXML
    private VBox box4;

    @FXML
    private VBox box1;

    @FXML
    private VBox box2;

    @FXML
    private Button buttonSchema3;

    @FXML
    private Button buttonSchema4;

    @FXML
    private Label remainingSeconds;

    private SchemaPanel schemaPanel1, schemaPanel2, schemaPanel3, schemaPanel4;
    private ObjectiveView objectiveView;
    private AskSchemaCardFaceEvent receivedEvent;


    public GUISelectSchemaCardFace(RemoteView view) {
        super(view);
    }


    @Override
    public void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event) {
        receivedEvent = event;

        if(root==null) return;
        if(event==null) return;
        Platform.runLater(() -> {
            schemaPanel1.updateSchema(new Schema(event.getSchemas()[0].getFace(Side.FRONT)));
            schemaPanel2.updateSchema(new Schema(event.getSchemas()[0].getFace(Side.BACK)));
            schemaPanel3.updateSchema(new Schema(event.getSchemas()[1].getFace(Side.FRONT)));
            schemaPanel4.updateSchema(new Schema(event.getSchemas()[1].getFace(Side.BACK)));

            buttonSchema1.setText("Select " + event.getSchemas()[0].getFace(Side.FRONT).getName());
            buttonSchema2.setText("Select " + event.getSchemas()[0].getFace(Side.BACK).getName());
            buttonSchema3.setText("Select " + event.getSchemas()[1].getFace(Side.FRONT).getName());
            buttonSchema4.setText("Select " + event.getSchemas()[1].getFace(Side.BACK).getName());


            buttonSchema1.setDisable(false);
            buttonSchema2.setDisable(false);
            buttonSchema3.setDisable(false);
            buttonSchema4.setDisable(false);
        });

    }

    @Override
    public void setActive() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/SelectSchemaCardFace.fxml"));
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

            buttonSchema1 = (Button) scene.lookup("#buttonSchema1");
            buttonSchema2 = (Button) scene.lookup("#buttonSchema2");
            buttonSchema3 = (Button) scene.lookup("#buttonSchema3");
            buttonSchema4 = (Button) scene.lookup("#buttonSchema4");

            box1 = (VBox) scene.lookup("#box1");
            box2 = (VBox) scene.lookup("#box2");
            box3 = (VBox) scene.lookup("#box3");
            box4 = (VBox) scene.lookup("#box4");

            playerPrivateObjectiveBox = (VBox) scene.lookup("#playerPrivateObjective");

            remainingSeconds = (Label) scene.lookup("#remainingSeconds");


            buttonSchema1.setText("Wait...");
            buttonSchema2.setText("Wait...");
            buttonSchema3.setText("Wait...");
            buttonSchema4.setText("Wait...");

            buttonSchema1.setDisable(true);
            buttonSchema2.setDisable(true);
            buttonSchema3.setDisable(true);
            buttonSchema4.setDisable(true);


            schemaPanel1 = new SchemaPanel(null);
            schemaPanel2 = new SchemaPanel(null);
            schemaPanel3 = new SchemaPanel(null);
            schemaPanel4 = new SchemaPanel(null);
            objectiveView = new ObjectiveView();

            VBox.setVgrow(schemaPanel1, Priority.ALWAYS);
            VBox.setVgrow(schemaPanel2, Priority.ALWAYS);
            VBox.setVgrow(schemaPanel3, Priority.ALWAYS);
            VBox.setVgrow(schemaPanel4, Priority.ALWAYS);
            VBox.setVgrow(objectiveView, Priority.ALWAYS);

            box1.getChildren().add(schemaPanel1);
            box2.getChildren().add(schemaPanel2);
            box3.getChildren().add(schemaPanel3);
            box4.getChildren().add(schemaPanel4);
            playerPrivateObjectiveBox.getChildren().add(objectiveView);

            buttonSchema1.setOnAction(GUISelectSchemaCardFace.this);
            buttonSchema2.setOnAction(GUISelectSchemaCardFace.this);
            buttonSchema3.setOnAction(GUISelectSchemaCardFace.this);
            buttonSchema4.setOnAction(GUISelectSchemaCardFace.this);

            logsPanel = (ScrollPane) scene.lookup("#logs");

            remainingSeconds.setText("Waiting for game to start...");

            renderEventLog();

            root.setStyle("-fx-background-image: url('/gui/background.jpg');\n" +
                "    -fx-background-repeat: repeat;");

            showSchemaCardFaceSelection(receivedEvent);
        });


    }

    @Override
    public void setInactive() {
        if (root == null) return;
        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        });
    }

    @Override
    public void renderPrivateObjective(PrivateObjective privateObjective) {
        if (root == null) return;
        Platform.runLater(() -> objectiveView.setObjective(privateObjective));
    }


    @Override
    public void handle(ActionEvent event) {
        if (event.getSource().equals(buttonSchema1)) {
            selectFace(0, Side.FRONT);
        } else if (event.getSource().equals(buttonSchema2)) {
            selectFace(0, Side.BACK);
        } else if (event.getSource().equals(buttonSchema3)) {
            selectFace(1, Side.FRONT);
        } else if (event.getSource().equals(buttonSchema4)) {
            selectFace(1, Side.BACK);
        }

        buttonSchema1.setDisable(true);
        buttonSchema2.setDisable(true);
        buttonSchema3.setDisable(true);
        buttonSchema4.setDisable(true);
    }

    @Override
    protected void renderSecondsRemaining() {
        if (root == null) return;
        Platform.runLater(() -> remainingSeconds.setText("Remaining seconds: " + getSecondsRemaining()));
    }

    @Override
    public void renderEventLog() {

        ArrayList<LogEvent> pastEvents = getPastEvents();

        Platform.runLater(() -> {
            VBox events = new VBox();

            if (pastEvents.isEmpty()) {

                events.getChildren().add(new Label("No recent events."));
                logsPanel.setContent(events);
                return;

            } else {

                for (int i = 0; i < pastEvents.size(); i++) {
                    events.getChildren().add(new Label(new Date(pastEvents.get(i).getTimestamp()).getHours() + ":" + new Date(pastEvents.get(i).getTimestamp()).getMinutes() + ":" + new Date(pastEvents.get(i).getTimestamp()).getSeconds() + "  " + pastEvents.get(i).getMessage()));
                }

                logsPanel.setContent(events);
            }

        });

    }
}
