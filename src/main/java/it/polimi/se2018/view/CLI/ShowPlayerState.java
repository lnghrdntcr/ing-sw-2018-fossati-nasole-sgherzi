package it.polimi.se2018.view.CLI;

public class ShowPlayerState extends State{

    public ShowPlayerState(CLIGameTable gameTable) {
        super(gameTable);
    }

    @Override
    public State process(String input) {
        if(getGameTable().getPlayer(input)!=null){
            CLIPrinter.printPlayer(getGameTable().getPlayer(input));
            return new MainMenuState(getGameTable());
        }else{
            CLIPrinter.printError("No such player!");
            return this;
        }
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Insert the name of a player: ");
    }
}
