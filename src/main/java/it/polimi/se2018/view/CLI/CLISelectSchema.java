package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.CLISelectSchemaSubState;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.SelectSchemaCardFace;

public class CLISelectSchema extends SelectSchemaCardFace implements InputListenerThread.InputListener {

    private boolean iAmActive = false;
    private InputListenerThread inputListenerThread;
    private SchemaCardFace[] faces = new SchemaCardFace[4];
    private CLISelectSchemaSubState subState;
    private int choice;

    public CLISelectSchema(RemoteView view) {
        super(view);
    }

    @Override
    public void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event) {
        for (int i = 0; i < 4; i++) {
            faces[i] = event.getSchemas()[i / 2].getFace(i % 2 == 0 ? Side.FRONT : Side.BACK);
        }

        System.out.println("Choose your Schema Card: \n\n");

        //TODO
        for (int i = 0; i < 4; i++) {
            System.out.println(i + "for" + faces[i].getName());
        }

        subState = subState.CHOICE;

    }

    @Override
    public void setActive() {
        if (!iAmActive) {
            inputListenerThread = new InputListenerThread(this);
            inputListenerThread.start();
            iAmActive = true;
        }
    }

    @Override
    public void renderPrivateObjective() {
        //TODO
        System.out.println("Private objective assigned!");
    }

    @Override
    public void setInactive() {
        if (iAmActive) {
            if (inputListenerThread != null) inputListenerThread.kill();
            iAmActive = false;
        }
    }

    @Override
    public void onCommandRecived(String input) {
        //TODO:

        if (subState == subState.CHOICE) {
            try {
                choice = Integer.parseInt(input);
            } catch(RuntimeException e){
                System.out.println("Invalid choice, try again");
                return;
            }
            if (choice < 1 || choice > 4) System.out.println("Invalid choice, try again");
            else {
                subState = subState.USURE;
            }
        } else {
            System.out.println("You chose" + faces[choice - 1].getName() + "\nAre you sure? [Y] [N]");
            if(!input.equalsIgnoreCase("y")||!input.equalsIgnoreCase("n")) System.out.println("Invalid choice, try again");
            else{
                selectFace(choice / 2, choice % 2 == 0 ? Side.FRONT : Side.BACK);
            }
        }

    }
}
