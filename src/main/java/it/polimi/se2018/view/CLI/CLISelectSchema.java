package it.polimi.se2018.view.CLI;

import it.polimi.se2018.controller.controllerEvent.AskSchemaCardFaceEvent;
import it.polimi.se2018.view.RemoteView;
import it.polimi.se2018.view.SelectSchemaCardFace;

public class CLISelectSchema extends SelectSchemaCardFace {

    public CLISelectSchema(RemoteView view) {
        super(view);
    }

    @Override
    public void showSchemaCardFaceSelection(AskSchemaCardFaceEvent event) {


    }
}
