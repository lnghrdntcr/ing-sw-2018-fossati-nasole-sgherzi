package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.model_view.ToolCardImmutable;
import it.polimi.se2018.utils.Event;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The CLI state machine context
 */
public class CLI {
    private CLIState state;

    String playerName;
    private PrivateObjective myPrivateO;
    private ArrayList<PublicObjective> PublicOs;
    private SchemaCardFace myRestrictions;
    private ArrayList<SchemaCardFace> OtherRestrictions;
    private Schema mySchema;
    private ArrayList<Schema> shemas;
    private ArrayList<ToolCardImmutable> tool;
    private DraftBoardImmutable draftBoard;
    private DiceHolderImmutable diceHolder;

    private Scanner input = new Scanner(System.in);

    public CLI(String playerName){
        this.playerName=playerName;
        this.state=new CLIStart(playerName).doAction(this, null);
    }


    public ArrayList<ToolCardImmutable> getTool() {
        return tool;
    }

    public void setDraftBoard(DraftBoardImmutable draft) {
        this.draftBoard = draft;
    }

    public void setDiceHolder(DiceHolderImmutable dhi) {
        this.diceHolder = dhi;
    }

    public void setMyRestrictions(SchemaCardFace schemaCardFace) {
        this.myRestrictions = schemaCardFace;
    }



    public void handleEvent(Event e) {
        this.state = this.state.doAction(this, e);
    }

    public int askInt() {
        return input.nextInt();
    }

}
