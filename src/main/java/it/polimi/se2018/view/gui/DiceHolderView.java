package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.utils.Settings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.awt.*;

public class DiceHolderView extends HBox {
    private VBox[] turns = new VBox[Settings.TURNS];

    private int token, usedToken;

    private Schema schema;

    public DiceHolderView() {
        for (int x = 0; x < Settings.TURNS; x++) {
            turns[x] = new VBox();
            this.getChildren().add(turns[x]);
            turns[x].getChildren().add(new Label("Turn "+(x+1)));
        }
    }

    public void setDiceHolder(DiceHolderImmutable diceHolder){
        for(int x=0; x<Settings.TURNS; x++){
            for(int y=0; y<diceHolder.getDiceFaces(x).length; y++){
                turns[x].getChildren().removeAll();
                turns[x].getChildren().add(new Label("Turn "+(x+1)));
                Dice dice = new Dice();
                dice.setDiceFace(diceHolder.getDiceFaces(x)[y]);
                turns[x].getChildren().add(dice);
            }
        }
    }

}
