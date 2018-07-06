package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.EndGameEvent;
import it.polimi.se2018.utils.LeaderBoardHolder;
import it.polimi.se2018.utils.ScoreHolder;
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

    public void handleEndGameEvent(EndGameEvent event) {
        super.handleEndGameEvent(event);
        this.getView().activateGameEnding();
        CLIPrinter.printFinalAnimation();

        for (ScoreHolder sc : event.getLeaderBoard()) {
            CLIPrinter.printInfo(sc.getPlayerName());
        }

        CLIPrinter.printInfo("LeaderBoard: ");
        for (LeaderBoardHolder lbh: getGlobalLeaderboardUnpacked()){
            CLIPrinter.printInfo("Player: " + lbh.getName());
            CLIPrinter.printInfo("\tVictories " + lbh.getScores().optInt("victories", 0));
            CLIPrinter.printInfo("\tLosses " + lbh.getScores().optInt("losses", 0));
            CLIPrinter.printInfo("\tTotal time played " + lbh.getScores().optInt("totalTimePlayed", 0));
        }

        System.exit(0);
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
