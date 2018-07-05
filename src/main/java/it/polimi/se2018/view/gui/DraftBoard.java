package it.polimi.se2018.view.gui;

import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.utils.Log;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

/**
 * A class to draw the DraftBoard on the screen
 */
public class DraftBoard extends HBox {


    private OnDiceSelectedListener dicePlacer = null;

    public DraftBoard() {

    }

    public DraftBoard(OnDiceSelectedListener dicePlacer) {

        this.dicePlacer = dicePlacer;
    }

    /**
     * Updates the DraftBoard on the screen
     * @param draftBoard the {@link DraftBoardImmutable} to render
     */
    public void setDraftBoard(DraftBoardImmutable draftBoard) {

        if (draftBoard == null) return;

        Platform.runLater(() -> {

            this.getChildren().clear();


            for (int i = 0; i < draftBoard.getDices().length; i++) {

                Dice dice = new Dice();
                dice.setDiceFace(draftBoard.getDices()[i]);
                if (dicePlacer != null) {
                    final int index = i;
                    dice.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                        Log.d("Selected dice on index: " + index);
                        dicePlacer.onDiceSelected(index);
                    });
                }
                this.getChildren().add(dice);
            }

        });


    }

    /**
     * An interface to handle actions on the DraftBoard
     */
    public interface OnDiceSelectedListener {
        /**
         * Handles the click on a dice of the DraftBoard
         * @param index the index of the dice clicked
         */
        public void onDiceSelected(int index);
    }


}
