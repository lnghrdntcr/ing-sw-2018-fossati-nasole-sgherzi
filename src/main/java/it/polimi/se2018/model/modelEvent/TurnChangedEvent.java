package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

/**
 * Event to inform that the turn has changed
 */
public class TurnChangedEvent extends ModelEvent {
    private int round;
    private boolean direction;
    private final boolean dicePlaced;
    private final boolean toolcardUsed;

    public TurnChangedEvent(String emitter, String receiver, String player, int round, boolean direction, boolean dicePlaced, boolean toolcardUsed) {
        super(emitter, player, receiver);
        this.round = round;
        this.direction = direction;
        this.dicePlaced = dicePlaced;
        this.toolcardUsed = toolcardUsed;
    }

    public TurnChangedEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        round=jsonObject.getInt("round");
        direction=jsonObject.getBoolean("direction");
        dicePlaced=jsonObject.getBoolean("dicePlaced");
        toolcardUsed=jsonObject.getBoolean("toolcardUsed");

    }

    public int getRound() {
        return round;
    }

    public boolean getDirection() {
        return direction;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleTurnChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }

    public boolean isDicePlaced() {
        return dicePlaced;
    }

    public boolean isToolcardUsed() {
        return toolcardUsed;
    }


}
