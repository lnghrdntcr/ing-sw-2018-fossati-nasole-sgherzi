package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model_view.ToolCardImmutable;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

public class ToolCard extends StackPane {
    @FXML
    private ImageView toolcard;

    @FXML
    private Label token;



    public ToolCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/ToolCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    public void setToolCard(ToolCardImmutable toolCardImmutable){
        toolcard.setImage(new Image("gui/toolcards/"+toolCardImmutable.getName()+".png"));
        token.setText(Integer.toString(toolCardImmutable.getToken()));
    }

}
