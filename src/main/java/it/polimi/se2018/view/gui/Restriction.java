package it.polimi.se2018.view.gui;

import it.polimi.se2018.model.schema_card.CellRestriction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Cell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * A class to render a restriction of a {@link it.polimi.se2018.model.schema_card.SchemaCard} on the screen
 */
public class Restriction extends StackPane {
    @FXML
    private ImageView diceColour;
    @FXML
    private ImageView diceNumber;

    public Restriction() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("gui/Dice.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Updates the restriction on the screen
     * @param restriction the restriction to render
     */
    void setRestriction(CellRestriction restriction) {
        if (restriction.toString().equals("")) {
            diceColour.setImage(new Image("gui/restrictions/no.png"));
            return;
        }

        try {
            diceColour.setImage(new Image("gui/restrictions/number/" + Integer.parseInt(restriction.toString()) + ".png"));
            return;
        } catch (Exception ignored) {
        }

        diceColour.setImage(new Image("gui/restrictions/color/" + restriction.toString() + ".png"));

    }
}
