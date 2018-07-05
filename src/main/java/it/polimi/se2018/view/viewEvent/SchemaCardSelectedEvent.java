package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;
import it.polimi.se2018.model.schema_card.SchemaCardFace;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Event;
import org.json.JSONObject;

public class SchemaCardSelectedEvent extends ViewEvent {

    private final Side side;
    private int schemaCardId;

    public SchemaCardSelectedEvent(String emitter, String receiver , String player, int schemaCardId, Side side) {
        super(emitter, player, receiver);
        this.side = side;
        if (schemaCardId >= 0 && schemaCardId < 2) {
            this.schemaCardId = schemaCardId;
        } else
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": number must be between 0 and 1.");
    }

    public SchemaCardSelectedEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        schemaCardId=jsonObject.getInt("schemaCardId");
        Side toAssign = null;

        String sideJson = jsonObject.getString("side");
        for(Side tempSide: Side.values()){
            if(sideJson.equalsIgnoreCase(tempSide.toString())){
                toAssign=tempSide;
            }
        }

        side=toAssign;
    }

    public int getSchemaCardId() {
        return schemaCardId;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public State visit(State state) {
        return state.handleSchemaCardSelectedEvent(this);
    }

    public static void main(String a[]){
        System.out.println(new SchemaCardSelectedEvent("emitter", "receiver", "player", 1, Side.FRONT).toJSON().toString());
        System.out.println(new SchemaCardSelectedEvent(new SchemaCardSelectedEvent("emitter", "receiver", "player", 1, Side.FRONT).toJSON().toString()).toJSON().toString());
    }
}
