package it.polimi.se2018.view.gui;

import it.polimi.se2018.view.AbstractMainMenuState;
import it.polimi.se2018.view.GameTable;

public class GUIMainMenuState extends AbstractMainMenuState {
    public GUIMainMenuState(GameTable gameTable) {
        super(gameTable);
    }

    @Override
    public void process(String input) {

    }

    @Override
    public void unrealize() {
        //TODO: disable main view
    }

    @Override
    public void render() {
        //TODO: enable main view
    }
}
