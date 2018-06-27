package it.polimi.se2018.view.CLI;

public class ShowPlayerState extends State{

    public ShowPlayerState(CLIGameTable gameTable) {
        super(gameTable);
    }

    @Override
    public State process(String input) {
        if(input.equals("cancel")){
            return new CLIMainMenuState(getGameTable());
        }else if(getGameTable().getPlayer(input)!=null){
            CLIPrinter.printPlayer(getGameTable(), getGameTable().getPlayer(input));
            return new CLIMainMenuState(getGameTable());
        }else{
            CLIPrinter.printError("No such player!");
            return this;
        }
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
