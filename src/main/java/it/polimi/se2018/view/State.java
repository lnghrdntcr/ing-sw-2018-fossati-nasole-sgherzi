package it.polimi.se2018.view;

/**
 * A generic state in which the {@link GameTable} can be put.
 * Each state react in a different way to the events received by the Model and Controller
 */
public abstract class State implements Renderizable {


    private final GameTable gameTable;

    public State(GameTable gameTable){
        this.gameTable = gameTable;
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    /**
     * Process an input of a string from the command line
     * @param input the string to process
     */
    public abstract void process(String input);

    /**
     * Free the resources used by the class and remove any object on the screen
     */
    public abstract void unrealize();

}
