package it.polimi.se2018.model.schema;

import org.fusesource.jansi.Ansi;

/**
 * This enum contains the 5 possible colors in the game
 */
public enum GameColor {

    RED(Ansi.Color.RED), BLUE(Ansi.Color.BLUE), GREEN(Ansi.Color.GREEN), PURPLE(Ansi.Color.MAGENTA), YELLOW(Ansi.Color.YELLOW);

    Ansi.Color color;

    GameColor(Ansi.Color color) {
        this.color = color;
    }

}
