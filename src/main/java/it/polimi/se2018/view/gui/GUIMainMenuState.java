package it.polimi.se2018.view.gui;

import it.polimi.se2018.view.AbstractMainMenuState;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.InputError;
import javafx.scene.control.Alert;

public class GUIMainMenuState extends AbstractMainMenuState {
    public GUIMainMenuState(GameTable gameTable) {
        super(gameTable);
    }

    @Override
    public void process(String input) {

        int in = Integer.parseInt(input);
        try {
            processSelection(in);
        } catch (InputError ie) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText(ie.getMessage());
            alert.show();

        }

    }

    @Override
    public void unrealize() {
        ((GUIGameTable) getGameTable()).disableWindow();
    }

    @Override
    public void render() {
        ((GUIGameTable) getGameTable()).enableWindow();
    }
}
