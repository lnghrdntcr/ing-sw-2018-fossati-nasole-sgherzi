package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema_card.CellRestriction;
import it.polimi.se2018.model.schema_card.SchemaCardFace;

public class CLIMoveDice extends State {

    SchemaCardFace.Ignore ignore;
    String toolName;

    public CLIMoveDice(CLIGameTable gameTable, SchemaCardFace.Ignore ignore, String toolName) {
        super(gameTable);
        this.ignore=ignore;
        this.toolName=toolName;
    }

    //TODO

    @Override
    public State process(String input) {
        return null;
    }

    @Override
    public void render() {
        CLIPrinter.printQuestion("Choose a dice to move:");
        CLIPrinter.printSchema(getGameTable().getSchemas(getGameTable().getCurrentPlayer()));

    }
}
