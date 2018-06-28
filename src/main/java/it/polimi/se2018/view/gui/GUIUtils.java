package it.polimi.se2018.view.gui;

import javafx.scene.control.Alert;

public class GUIUtils {
    public static void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();

    }
}
