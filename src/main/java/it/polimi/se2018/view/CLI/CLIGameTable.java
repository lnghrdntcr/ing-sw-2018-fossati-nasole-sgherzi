package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.GameStartEvent;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.RemoteView;

public class CLIGameTable extends GameTable implements InputListenerThread.InputListener {

    private boolean iAmActive = false;
    private InputListenerThread inputListenerThread;
    private State realeState;

    public CLIGameTable(RemoteView view) {
        super(view);
        this.realeState = new MainMenuState(this);
    }

    @Override
    protected void renderDiceHolder() {
        System.out.println("DiceHolder Changed.");
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
        getView().activateGameTable();
    }

    @Override
    public void setActive(){
        Log.d("CLIGAMETABLE ACTIVE");
        if(!iAmActive){
            inputListenerThread = new InputListenerThread(this);
            inputListenerThread.start();
            iAmActive = true;
        }

        this.realeState.render();

    }
    @Override
    public void setInactive(){
        Log.d("CLIGAMETABLE INACTIVE");
        if(iAmActive){
            if(inputListenerThread != null) inputListenerThread.kill();
            iAmActive = false;
        }
    }

    @Override
    public void handleGameStart(GameStartEvent gameStartEvent) {
        renderTurn();
    }

    @Override
    public void onCommandRecived(String input) {
        // TODO: WIP
        this.realeState = this.realeState.process(input);

    }
}
