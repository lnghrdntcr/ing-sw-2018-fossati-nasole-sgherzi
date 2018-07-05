package it.polimi.se2018.view.gui;

import javafx.scene.control.Alert;

/**
 * Utils class that contains static functions to help the build of the gui
 */
public class GUIUtils {
    public static void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.show();

    }
}
