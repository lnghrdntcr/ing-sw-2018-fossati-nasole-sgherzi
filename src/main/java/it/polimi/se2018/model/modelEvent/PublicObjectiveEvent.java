package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;

public class PublicObjectiveEvent extends ModelEvent {
    private PublicObjective publicObjective;
    private int index;

    public PublicObjectiveEvent(String emitter, String receiver,String player, PublicObjective publicObjective, int index) {
        super(emitter, player, receiver);
        this.publicObjective = publicObjective;
        this.index = index;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handlePublicObjective(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }

    public PublicObjective getPublicObjective() {
        return publicObjective;
    }

    public int getIndex() {
        return index;
    }

    public static void main(String a[]){

    }
}
