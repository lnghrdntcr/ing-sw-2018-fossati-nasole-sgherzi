package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.AbstractPlaceDiceState;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class GUIPlaceDice extends AbstractPlaceDiceState implements DraftBoard.OnDiceSelectedListener, EventHandler<ActionEvent>, SchemaPanel.OnSchemaInteractionListener {

    @FXML
    private HBox root;

    @FXML
    private VBox schemaBox;

    @FXML
    private VBox draftBox;

    @FXML
    private Label messages;

    @FXML
    private Button buttonOne;

    @FXML
    private Button buttonTwo;

    @FXML
    private Button buttonThree;

    @FXML
    private Button buttonFour;

    @FXML
    private Button buttonFive;

    @FXML
    private Button buttonSix;

    private SchemaPanel schemaView;
    private DraftBoard draftBoardView;
    private Dice dice;

    public GUIPlaceDice(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness) {
        super(gameTable, ignore, isFromTool, forceLoneliness);
    }

    public GUIPlaceDice(GameTable gameTable, SchemaCardFace.Ignore ignore, boolean isFromTool, boolean forceLoneliness, int forceDice, boolean shouldSelectNumber) {
        super(gameTable, ignore, isFromTool, forceLoneliness, forceDice, shouldSelectNumber);
    }


    public void render() {

        if (root == null) {

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

                secondStage.initOwner(((GUIGameTable) getGameTable()).getWindow());
                secondStage.initModality(Modality.APPLICATION_MODAL);

                secondStage.show();


                schemaBox = (VBox) scene.lookup("#schemaBox");
                draftBox = (VBox) scene.lookup("#draftBox");
                messages = (Label) scene.lookup("#messages");
                buttonOne = (Button) scene.lookup("#buttonOne");
                buttonTwo = (Button) scene.lookup("#buttonTwo");
                buttonThree = (Button) scene.lookup("#buttonThree");
                buttonFour = (Button) scene.lookup("#buttonFour");
                buttonFive = (Button) scene.lookup("#buttonFive");
                buttonSix = (Button) scene.lookup("#buttonSix");

                this.schemaView = new SchemaPanel(this);
                this.draftBoardView = new DraftBoard(this);
                this.dice = new Dice();

                schemaView.updateSchema(getGameTable().getSchema(getGameTable().getView().getPlayer()));
                draftBoardView.setDraftBoard(getGameTable().getDraftBoardImmutable());

/*                root.getChildren().add(schemaView);
                root.getChildren().add(draftBoardView);*/


                schemaBox.getChildren().add(schemaView);
                draftBox.getChildren().add(0, draftBoardView);
                draftBox.getChildren().add(2, dice);


                buttonOne.setOnAction(this);
                buttonTwo.setOnAction(this);
                buttonThree.setOnAction(this);
                buttonFour.setOnAction(this);
                buttonFive.setOnAction(this);
                buttonSix.setOnAction(this);

                secondStage.setOnCloseRequest((windowEvent) -> {
                    processCancel();
                });


            });


        }

        Platform.runLater(() -> {

            if (getInternalState() == InternalState.POSITION_SELECTION) {

                messages.setText("Select a cell on the schema");

                setButtonVisible(false);

                this.dice.setVisible(false);
                this.schemaView.setDisable(false);
                this.draftBoardView.setDisable(true);

                schemaView.highlightAllowedPoints(getGameTable().getDraftBoardImmutable().getDices()[getSelectedDice()], getIgnore(), isForceLoneliness());

            } else if (getInternalState() == InternalState.DICE_SELECTION) {

                messages.setText("Select a dice");

                setButtonVisible(false);

                this.dice.setVisible(false);
                this.schemaView.setDisable(true);
                this.draftBoardView.setDisable(false);


            } else if (getInternalState() == InternalState.NUMBER_SELECTION) {

                messages.setText("This dice was drawn, select its number");

                dice.setDiceFace(getGameTable().getDraftBoardImmutable().getDices()[getSelectedDice()]);


                setButtonVisible(true);
                this.dice.setVisible(true);
                this.schemaView.setDisable(true);
                this.draftBoardView.setDisable(true);

            }
        });


    }

    private void setButtonVisible(boolean b) {

        buttonOne.setVisible(b);
        buttonTwo.setVisible(b);
        buttonThree.setVisible(b);
        buttonFour.setVisible(b);
        buttonFive.setVisible(b);
        buttonSix.setVisible(b);

    }

    @Override
    public void onDiceSelected(int index) {
        if (getInternalState() != InternalState.DICE_SELECTION) return;
        try {
            Log.d("Selected dice on index: " + index + " <== Going to process");
            processDiceSelection(index);
        } catch (InputError ie) {
            GUIUtils.showError(ie.getMessage());
        }
    }

    @Override
    public void process(String input) {

    }

    @Override
    public void unrealize() {
        if (root == null) return;
        ((Stage) (root.getScene().getWindow())).close();
    }

    @Override
    public void handle(ActionEvent event) {

        if (getInternalState() != InternalState.NUMBER_SELECTION) return;

        int selection = Integer.parseInt(((Button) event.getSource()).getText());
        try {
            processNumberSelected(selection);
        } catch (InputError ie) {
            GUIUtils.showError(ie.getMessage());
        }

    }

    @Override
    public void onCellSelected(Point point) {
        if (getInternalState() == InternalState.POSITION_SELECTION) {
            try {
                processPositionSelected(point);
            } catch (InputError ie) {
                GUIUtils.showError(ie.getMessage());
            }

        }
    }
}
