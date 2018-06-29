package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.utils.Settings;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.Point;

public class DiceHolderView extends HBox implements EventHandler<javafx.scene.input.MouseEvent> {

    private VBox[] turns = new VBox[Settings.TURNS];

    private int token, usedToken;

    private Schema schema;
    private OnTurnHolderInteractionListener interactionListener;

    public DiceHolderView() {
        this(null);
    }

    public DiceHolderView(OnTurnHolderInteractionListener interactionListener) {

        this.interactionListener = interactionListener;

        for (int x = 0; x < Settings.TURNS; x++) {


            turns[x] = new VBox();
            this.getChildren().add(turns[x]);

            turns[x].getChildren().add(new Label("Turn " + (x + 1) + " "));

        }
    }

    public void setDiceHolder(DiceHolderImmutable diceHolder) {

        if (diceHolder == null) return;

        Platform.runLater(() -> {

            for (int x = 0; x < Settings.TURNS; x++) {

                turns[x].getChildren().clear();
                turns[x].getChildren().add(new Label("Turn " + (x + 1)));

                for (int y = 0; y < diceHolder.getDiceFaces(x).length; y++) {

                    Dice dice = new Dice();
                    dice.setDiceFace(diceHolder.getDiceFaces(x)[y]);

                    final int x1 = x, y1 = y;

                    dice.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, this);

                    turns[x].getChildren().add(dice);


                }

            }

        });

    }


    @Override
    public void handle(javafx.scene.input.MouseEvent event) {

    }

    public interface OnTurnHolderInteractionListener {
        public void onDiceSelected(Point point);
    }

}
