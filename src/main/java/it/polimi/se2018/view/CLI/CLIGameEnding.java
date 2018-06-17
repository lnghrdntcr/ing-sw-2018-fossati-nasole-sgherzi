package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.RemoteView;

public class CLIGameEnding extends GameEnding implements InputListenerThread.InputListener {

    private boolean iAmActive = false;

    public CLIGameEnding(RemoteView view) {
        super(view);
    }

    @Override
    public void onCommandRecived(String input) {
        // TODO: Terminate the game. Or see stats.
    }

    public void handleEndGameEvent(EndGameEvent event){
        this.getView().activateGameEnding();
        System.out.println("The winner that takes it all " + event.getLeaderBoard().get(0).getPlayerName());
    }

    @Override
    public void setActive() {
        if (!iAmActive) {
            //inputListenerThread = new InputListenerThread(this);
            //inputListenerThread.start();
            InputListenerThread.getInstance().setInputListener(this);
            iAmActive = true;
        }
    }

    @Override
    public void setInactive() {
        if (iAmActive) {
            iAmActive = false;
        }
    }
}
