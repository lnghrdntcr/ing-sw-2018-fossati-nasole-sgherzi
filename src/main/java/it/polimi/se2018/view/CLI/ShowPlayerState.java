package it.polimi.se2018.view.CLI;

import it.polimi.se2018.view.AbstractMainMenuState;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.State;

public class ShowPlayerState extends State {

    public ShowPlayerState(GameTable gameTable) {
        super(gameTable);
    }

    @Override
    public void process(String input) {
        if(input.equals("cancel")){
            getGameTable().setState( AbstractMainMenuState.createFromContext(getGameTable()));
        }else if(getGameTable().getPlayer(input)!=null){
            CLIPrinter.printPlayer(getGameTable(), getGameTable().getPlayer(input));
            getGameTable().setState( AbstractMainMenuState.createFromContext(getGameTable()));
        }else{
            CLIPrinter.printError("No such player!");
            getGameTable().setState( this);
        }
    }

    @Override
    public void unrealize() {

    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Available players:");
        for(String name: getGameTable().getPlayers()){
            CLIPrinter.printQuestion(name);
        }
        CLIPrinter.printQuestion("Insert the name of a player or cancel: ");
    }
}
