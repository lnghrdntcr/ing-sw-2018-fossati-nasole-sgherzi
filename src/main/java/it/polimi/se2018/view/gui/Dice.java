package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

public class Dice extends StackPane {
    @FXML
    private ImageView diceNumber;

    @FXML
    private ImageView diceColour;

    public Dice() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/Dice.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void setDiceFace(DiceFace diceFace){
        if(diceFace==null){
            diceColour.setImage(null);
            diceNumber.setImage(null);
            return;
        }

        diceColour.setImage(new Image("gui/dice/color/"+diceFace.getColor().toString()+".png"));
        diceNumber.setImage(new Image("gui/dice/number/"+diceFace.getNumber()+".png"));

    }

}

