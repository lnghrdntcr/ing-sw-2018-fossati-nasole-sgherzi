package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;

import java.util.HashMap;
import java.util.Scanner;

public class CLISchemaChoice implements CLIState {
    @Override
    public CLIState doAction(CLI cli, Event e) {
        AskSchemaCardFaceEvent event = (AskSchemaCardFaceEvent) e;
        SchemaCardFace[] faces = new SchemaCardFace[4];
        for(int i = 0; i<4; i++){
            faces[i]= event.getSchemas()[i/2].getFace(i%2==0?Side.FRONT:Side.BACK);
        }

        System.out.println("Choose your Schema Card: \n\n");

        for(int i=0; i<4; i++){
            System.out.println(i + "for" + faces[i].getName());
        }

        int choice =0;

        while(choice<1 || choice>4){
            choice = cli.askInt();
            if(choice<1 || choice>4) System.out.println("Choice not valid. Please: try again...");
        }

        cli.setMyRestrictions(faces[choice-1]);

        //TODO: magari continua altri schema

        return null;
    }
}
