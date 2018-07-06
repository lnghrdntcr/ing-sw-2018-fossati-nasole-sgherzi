package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.GameStartEvent;
import it.polimi.se2018.controller.controllerEvent.PlayerTimeoutEvent;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.RemoteView;

public class CLIGameTable extends GameTable implements InputListenerThread.InputListener {

    private boolean iAmActive = false;


    public CLIGameTable(RemoteView view) {
        super(view);
    }


    @Override
    public void handlePlayerTimeout(PlayerTimeoutEvent event) {
        if (event.getPlayerName().equals(getView().getPlayer())) {
            CLIPrinter.printError("Time is up!");
        }

        super.handlePlayerTimeout(event);
    }

    @Override
    protected void renderDiceHolder() {
        Log.d("DiceHolder Changed.");
    }

    @Override
    protected void renderLogEvent() {

    }

    @Override
    protected void renderDraftBoard() {
        System.out.println("Draftboard Changed.");
    }

    @Override
    protected void renderPlayer(String player) {
    }

    @Override
    protected void renderSchema(String player) {

    }

    @Override
    protected void renderToolcard(int index) {

    }

    @Override
    protected void renderPublicObjective(int index) {

    }

    @Override
    protected void renderTurn() {
    }

    @Override
    public void setActive() {
        Log.d("CLIGAMETABLE ACTIVE");
        if (!iAmActive) {
            InputListenerThread.getInstance().setInputListener(this);
            iAmActive = true;
        }

        this.getRealeState().render();

    }

    @Override
    public void setInactive() {
        Log.d("CLIGAMETABLE INACTIVE");
        if (iAmActive) {
            iAmActive = false;
        }
    }

    @Override
    public void renderTimeOut() {
        if (getSecondsRemaining() % 10 == 0) {
            if (getCurrentPlayer() != null)
                if (getCurrentPlayer().equals(getView().getPlayer())) {
                    CLIPrinter.printError("Remaining seconds: " + getSecondsRemaining() + ".");
                } else {
                    CLIPrinter.printError("Player " + getCurrentPlayer() + " has " + getSecondsRemaining() + " seconds left.");
                }
            else {
                CLIPrinter.printError(getSecondsRemaining() + " seconds left.");
            }
        }
    }

    @Override
    public void handleGameStart(GameStartEvent gameStartEvent) {
        renderTurn();
    }

    @Override
    public void onCommandRecived(String input) {
        getRealeState().process(input);
    }
}
