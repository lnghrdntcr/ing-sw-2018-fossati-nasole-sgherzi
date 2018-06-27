package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.utils.Log;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.awt.*;

public class DraftBoard extends HBox {


    private DicePlacer dicePlacer = null;

    public DraftBoard() {

    }

    public DraftBoard(DicePlacer dicePlacer) {

        this.dicePlacer = dicePlacer;
    }

    public void setDraftBoard(DraftBoardImmutable draftBoard) {

        if (draftBoard == null) return;

        Log.d("Calling setDraftBoard on " + draftBoard.toString());

        Platform.runLater(() -> {

            this.getChildren().clear();


            for (int i = 0; i < draftBoard.getDices().length - 1; i++) {

                Dice dice = new Dice();
                dice.setDiceFace(draftBoard.getDices()[i]);
                if (dicePlacer != null) {
                    final int index = i;
                    dice.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                        dicePlacer.handleMouseClickOnDice(index);
                    });
                }
                this.getChildren().add(dice);
            }

        });


    }

    public interface DicePlacer {
        public void handleMouseClickOnDice(int index);
    }


}
