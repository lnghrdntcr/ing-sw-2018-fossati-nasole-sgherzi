package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Log;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.SelectSchemaCardFace;

/**
 * the CLI implementation for the selection of the schema.
 */

public class CLISelectSchema extends SelectSchemaCardFace implements InputListenerThread.InputListener {

    private boolean iAmActive = false;
    private InputListenerThread inputListenerThread;
    private SchemaCardFace[] faces = new SchemaCardFace[4];
    private CLISelectSchemaSubState subState = CLISelectSchemaSubState.CHOICE;
    private int choice;

    public CLISelectSchema(RemoteView view) {
        super(view);
    }

    /**
     * Shows the schema cards the player can choose via CLI.
     * @param event the model event asking the choice of the schema in a set of 4.
     */
    @Override
    public void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event) {
        for (int i = 0; i < 4; i++) {
            faces[i] = event.getSchemas()[i / 2].getFace(i % 2 == 0 ? Side.FRONT : Side.BACK);
        }

        System.out.println("Choose your Schema Card: \n\n");

        for (int i = 0; i < 4; i++) {
            System.out.println(i+1 + "for:\n");
            CLIPrinter.printSchemaCardFace(faces[i]);
        }

        subState = CLISelectSchemaSubState.CHOICE;

    }

    /**
     * Shows the player's private objective as soon as it's assigned.
     */
    @Override
    public void renderPrivateObjective(PrivateObjective privateObjective) {
        CLIPrinter.printPrivateObjective(privateObjective);
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
    public void setInactive() {
        if (iAmActive) {
            if (inputListenerThread != null) inputListenerThread.kill();
            iAmActive = false;
        }
    }

    /**
     * Reacts to players' inputs
     * @param input the input
     */
    @Override
    public void onCommandRecived(String input) {

        if (subState == CLISelectSchemaSubState.CHOICE) {
            try {
                choice = Integer.parseInt(input);

            } catch (RuntimeException e) {
                System.out.println("Invalid choice, try again");
                return;
            }
            if (choice < 1 || choice > 4) System.out.println("Invalid choice, try again");
            else {
                subState = CLISelectSchemaSubState.USURE;
                System.out.println("You chose" + faces[choice - 1].getName() + "\nAre you sure? [Y] [N]");
            }
        } else if(subState == CLISelectSchemaSubState.USURE) {
            if (!input.trim().equalsIgnoreCase("y") && !input.trim().equalsIgnoreCase("n"))
                System.out.println("Invalid choice, try again");
            else {
                if(input.trim().equalsIgnoreCase("y")) {
                    selectFace(choice / 2, choice % 2 == 0 ? Side.FRONT : Side.BACK);
                    subState = CLISelectSchemaSubState.END;
                } else {
                    subState = CLISelectSchemaSubState.CHOICE;
                }
            }

        } else {
            CLIPrinter.printQuestion("Waitin for other people...");
        }

    }

    public enum CLISelectSchemaSubState {
        CHOICE, USURE, END;
    }
}
