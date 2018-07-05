package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.objectives.ColorVariety;
import it.polimi.se2018.model.objectives.PublicObjective;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

public class PublicObjectiveEvent extends ModelEvent {
    private String publicObjective;
    private int index;

    public PublicObjectiveEvent(String emitter, String receiver, String player, PublicObjective publicObjective, int index) {
        super(emitter, player, receiver);
        this.publicObjective = publicObjective.getClass().getSimpleName();
        this.index = index;
    }

    public PublicObjectiveEvent(String json) {
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        publicObjective = jsonObject.getString("publicObjective");
        index = jsonObject.getInt("index");
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

    public String getPublicObjective() {
        return publicObjective;
    }

    public int getIndex() {
        return index;
    }


}
