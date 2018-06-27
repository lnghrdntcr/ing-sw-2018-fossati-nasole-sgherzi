package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIPlaceDice extends HBox implements DraftBoard.DicePlacer {

    @FXML
    private HBox root;

    private final SchemaPanel schemaView;
    private final DraftBoard draftBoardView;

    private final Schema schema;
    private final DraftBoardImmutable draftBoard;
    private SchemaCardFace.Ignore ignore;

    public GUIPlaceDice(Schema schema, DraftBoardImmutable draftBoard) {

        this(schema, draftBoard, SchemaCardFace.Ignore.NOTHING);

    }

    public GUIPlaceDice(Schema schema, DraftBoardImmutable draftBoard, SchemaCardFace.Ignore ignore) {

        this.schema = schema;
        this.draftBoard = draftBoard;
        this.ignore = ignore;

        this.schemaView = new SchemaPanel();
        this.draftBoardView = new DraftBoard(this);

    }

    public void render() {


        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gui/DiceSelection.fxml"));

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

            schemaView.updateSchema(schema);
            draftBoardView.setDraftBoard(draftBoard);

            root.getChildren().add(schemaView);
            root.getChildren().add(draftBoardView);

            secondStage.show();

        });

    }

    @Override
    public void handleMouseClickOnDice(int index) {
        schemaView.highlightAllowedPoints(draftBoard.getDices()[index], ignore, false);
    }
}
