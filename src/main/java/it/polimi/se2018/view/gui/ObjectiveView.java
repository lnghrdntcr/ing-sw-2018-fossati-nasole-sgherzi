package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.objectives.Objective;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Log;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * A class to render an Objective card on the screen
 */
public class ObjectiveView extends StackPane {
    @FXML
    private ImageView objectiveIm;

    public ObjectiveView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/Objectives.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    /**
     * Updates the current displayed card on the screen. If null is passed as param, the back of the card is shown
     * @param objective the objective card to render
     */
    public void setObjective(Object objective){

        if(objective == null){

            objectiveIm.setImage(
                new Image("gui/objectives/private/back.png"));
            return;
        }

        if(objective instanceof PrivateObjective) {
            objectiveIm.setImage(
                    new Image("gui/objectives/private/" + ((PrivateObjective)objective).getColor().toString().toUpperCase() + ".png"));
            return;
        }

        objectiveIm.setImage(new Image("gui/objectives/public/" + ((String)objective) + ".png"));


    }

}

