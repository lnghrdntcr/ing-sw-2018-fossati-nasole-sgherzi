package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.utils.Log;
import javafx.application.Platform;
import javafx.scene.layout.HBox;

public class DraftBoard extends HBox {


    public DraftBoard() {

    }

    public void setDraftBoard(DraftBoardImmutable draftBoard) {

        if (draftBoard == null) return;

        Log.d("Calling setDraftBoard on " + draftBoard.toString());

        Platform.runLater(() -> {

            this.getChildren().clear();

            for (DiceFace df : draftBoard.getDices()) {

                Dice dice = new Dice();
                dice.setDiceFace(df);
                this.getChildren().add(dice);

            }
        });


    }


}
