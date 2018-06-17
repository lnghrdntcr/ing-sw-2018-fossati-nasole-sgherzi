package it.polimi.se2018.view.CLI;

import it.polimi.se2018.utils.Log;

import java.util.Scanner;

public class InputListenerThread extends Thread {

    private Scanner scanner;
    private InputListener inputListener;
    private boolean goAhead = true;

    private static InputListenerThread instance;


    private InputListenerThread(){super("InputListenerThread");}

    @Override
    public void run() {
        scanner = new Scanner(System.in);
        while (goAhead){
            try {
                String input = scanner.nextLine();
                if(inputListener!=null) {
                    inputListener.onCommandRecived(input);
                }
            } catch (RuntimeException ignored){
                Log.d("Input listener " + ignored.getMessage());
                Log.d(inputListener.getClass().getName());
                goAhead = false;
            }
        }
    }

    void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }

    public interface InputListener {
        public void onCommandRecived(String input);
    }

    public synchronized static InputListenerThread getInstance(){
        if(instance== null){
            instance=new InputListenerThread();
            instance.start();
        }

        return instance;
    }

}
