package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.awt.*;

/**
 * A class to render a player schema on the screen
 */
public class SchemaPanel extends GridPane {
    private Restriction[][] restrictions = new Restriction[Settings.CARD_WIDTH][Settings.CARD_HEIGHT];
    private Dice[][] dice = new Dice[Settings.CARD_WIDTH][Settings.CARD_HEIGHT];
    private Pane[][] pane = new Pane[Settings.CARD_WIDTH][Settings.CARD_HEIGHT];
    private Label cardName;
    private HBox difficultyContainer;

    private int token, usedToken;

    private Schema schema;
    private OnSchemaInteractionListener listener;

    public SchemaPanel(OnSchemaInteractionListener listener) {
        this.listener = listener;
        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(x);
            columnConstraints.setHgrow(Priority.ALWAYS);
            columnConstraints.prefWidthProperty().setValue(60);
            getColumnConstraints().addAll(columnConstraints);
        }


        for (int y = 0; y < Settings.CARD_HEIGHT + 1; y++) {
            RowConstraints rowConstraints = new RowConstraints(y);
            rowConstraints.prefHeightProperty().setValue(y == Settings.CARD_HEIGHT ? 20 : 60);
            rowConstraints.setVgrow(Priority.ALWAYS);
            getRowConstraints().add(rowConstraints);
        }


        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                final int _x = x, _y = y;
                restrictions[x][y] = new Restriction();
                GridPane.setConstraints(restrictions[x][y], x, y);
                GridPane.setMargin(restrictions[x][y], new Insets(5, 5, 5, 5));
                this.getChildren().addAll(restrictions[x][y]);


                dice[x][y] = new Dice();
                GridPane.setConstraints(dice[x][y], x, y);
                GridPane.setMargin(dice[x][y], new Insets(5, 5, 5, 5));
                this.getChildren().addAll(dice[x][y]);


                pane[x][y] = new Pane();
                GridPane.setConstraints(pane[x][y], x, y);
                GridPane.setMargin(pane[x][y], new Insets(5, 5, 5, 5));
                this.getChildren().addAll(pane[x][y]);
                pane[x][y].setOnMouseClicked(event -> clicked(new Point(_x, _y)));
            }
        }

        setStyle("-fx-background-color:#000000;");

        //Name of the card
        cardName = new Label();
        GridPane.setConstraints(cardName, 0, Settings.CARD_HEIGHT, Settings.CARD_WIDTH, 1);
        GridPane.setFillWidth(cardName, true);
        GridPane.setHalignment(cardName, HPos.CENTER);
        getChildren().add(cardName);
        cardName.setStyle("-fx-font: 20px Tahoma;\n" +
            "-fx-text-fill: #FFFFFFFF;\n" +
            "-fx-font-weight: bold;\n" +
            "-fx-text-alignment: center;");

        //difficulty
        difficultyContainer = new HBox();
        GridPane.setConstraints(difficultyContainer, Settings.CARD_WIDTH - 1, Settings.CARD_HEIGHT, 1, 1);
        GridPane.setFillWidth(difficultyContainer, true);
        GridPane.setHalignment(difficultyContainer, HPos.LEFT);

        getChildren().add(difficultyContainer);


    }

    /**
     * Update the schema on the screen
     * @param schema the schema to represent
     */
    public void updateSchema(Schema schema) {
        if(schema==null) return;
        this.schema = schema;
        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                restrictions[x][y].setRestriction(schema.getSchemaCardFace().getRestriction(new Point(x, y)));
                dice[x][y].setDiceFace(schema.getDiceFace(new Point(x, y)));
            }
        }

        cardName.setText(schema.getSchemaCardFace().getName());

        this.token = schema.getSchemaCardFace().getDifficulty();
        updateToken();

    }

    private void updateToken() {
        difficultyContainer.getChildren().clear();
        for (int i = 0; i < token; i++) {
            Circle circle = new Circle();
            circle.setRadius(5);
            if (i < usedToken) {
                circle.setStyle("-fx-fill: #FF0000FF;");
            } else {
                circle.setStyle("-fx-fill: #FFFFFFFF;");
            }
            if (i != 0) HBox.setMargin(circle, new Insets(0, 0, 0, 2));


            difficultyContainer.getChildren().add(circle);

        }
    }

    /**
     * Update the number of the token used by the player
     * @param usedToken the token used by the player
     */
    public void updateToken(int usedToken) {
        this.usedToken = usedToken;
        updateToken();
    }

    private void clicked(Point point) {
        Log.i("Clicked: " + point.x + ":" + point.y);

        if (listener != null) listener.onCellSelected(point);

    }

    /**
     * Hignlights the position where the user can place the given dice
     * @param dice the dice to place
     * @param ignore the restriction to relax
     * @param forceLoneliness if the dice must be placed alone
     */
    public void highlightAllowedPoints(DiceFace dice, SchemaCardFace.Ignore ignore, boolean forceLoneliness) {
        for (int x = 0; x < Settings.CARD_WIDTH; x++) {
            for (int y = 0; y < Settings.CARD_HEIGHT; y++) {
                if (schema.isDiceAllowed(new Point(x, y), dice, ignore, forceLoneliness)) {
                    pane[x][y].setStyle("-fx-background-color:#FF000066;\n" +
                        "-fx-border-color: orange;\n" +
                        "    -fx-border-width: 5;\n" +
                        "-fx-border-radius: 5");
                } else {
                    pane[x][y].setStyle("-fx-background-color:#00000000;\n" +
                        "-fx-border-color: none;\n" +
                        "    -fx-border-width: 0;\n" +
                        "-fx-border-radius: 0");
                }
            }
        }
    }


    /**
     * An interface to represent the interaction with the schema
     */
    public interface OnSchemaInteractionListener {
        /**
         * Handles the click of the user on a cell
         * @param point the coordinates of the clicked cell
         */
        void onCellSelected(Point point);
    }

}
