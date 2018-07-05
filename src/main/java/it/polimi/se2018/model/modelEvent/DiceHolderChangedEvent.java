package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.DiceHolder;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

/**
 * Event that is used to inform that the DiceHolder has been changed
 */
public class DiceHolderChangedEvent extends ModelEvent {
    private final DiceHolderImmutable diceHolderImmutable;

    public DiceHolderChangedEvent(String emitter, String receiver, String player, DiceHolderImmutable diceHolderImmutable) {
        super(emitter, player, receiver);
        this.diceHolderImmutable = diceHolderImmutable;
    }

    public DiceHolderChangedEvent(String json){
        super(json);
        diceHolderImmutable = DiceHolderImmutable.fromJSON(new JSONObject(json).getJSONObject("diceHolderImmutable"));
    }

    public DiceHolderImmutable getDiceHolderImmutable() {
        return diceHolderImmutable;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleDiceHolderChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();
        jsonObject.put("diceHolderImmutable", getDiceHolderImmutable().toJSON());

        return jsonObject;
    }
}
