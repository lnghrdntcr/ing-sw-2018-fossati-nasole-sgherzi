package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Log;

import java.util.Scanner;

public class InputListenerThread extends Thread {

    private Scanner scanner;
    private InputListener inputListener;
    private boolean goAhead;


    public InputListenerThread(InputListener inputListener){
        this.inputListener = inputListener;
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        while (goAhead){
            try {
                inputListener.onCommandRecived(scanner.nextLine());
            } catch (IllegalStateException ignored){}
        }
    }

    public void kill(){
        goAhead = false;
        scanner.close();
    }

    public interface InputListener {
        public void onCommandRecived(String input);
    }

}
