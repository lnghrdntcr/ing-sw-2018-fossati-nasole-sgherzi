package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Log;

import java.util.Scanner;

public class InputListenerThread extends Thread {

    private Scanner scanner;
    private InputListener inputListener;
    private boolean goAhead = true;


    public InputListenerThread(InputListener inputListener){
        this.inputListener = inputListener;
    }

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        while (goAhead){
            try {
                String input = scanner.nextLine();
                inputListener.onCommandRecived(input);
            } catch (RuntimeException ignored){
                Log.d("Input listener " + ignored.getMessage());
                Log.d(inputListener.getClass().getName());
                goAhead = false;
            }
        }
    }

    public void kill(){
        goAhead = false;
        // TODO: fix the hangin in here.
        scanner.close();
    }

    public interface InputListener {
        public void onCommandRecived(String input);
    }

}
