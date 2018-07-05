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

/**
 * A class to render the dice Holder on the screen
 */
public class DiceHolderView extends HBox  {

    private VBox[] turns = new VBox[Settings.TURNS];


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

    /**
     * Updates the dice holder on the screen
     * @param diceHolder the dice holder to render
     */
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

                    dice.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> interactionListener.onDiceSelected(new Point(x1, y1)));

                    turns[x].getChildren().add(dice);


                }

            }

        });

    }

    /**
     * An interface that represent a listener for an actionon the TurnHolder
     */
    public interface OnTurnHolderInteractionListener {
        /**
         * Handles the action of the click on a specific dice
         * @param point the coordinates of the dice clicked
         */
        public void onDiceSelected(Point point);
    }

}
