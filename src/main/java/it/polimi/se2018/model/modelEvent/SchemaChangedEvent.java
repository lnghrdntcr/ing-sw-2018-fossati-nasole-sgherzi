package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model.schema.Schema;
import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.utils.Settings;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * An event to inform that a player's schema has been changed
 */
public class SchemaChangedEvent extends ModelEvent {
    Schema schema;

    public SchemaChangedEvent(String emitter, String receiver, String player, Schema schema) {
        super(emitter, player, receiver);
        this.schema = schema;
    }

    public SchemaChangedEvent(String json){
        super(json);
        if(!new JSONObject(json).isNull("schema")) {
            schema = Schema.fromJSON(new JSONObject(json).getJSONObject("schema"));
        }
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleSchemaChanged(this);
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

        jsonObject.put("schema", getSchema()==null?null:getSchema().toJSON());

        return jsonObject;
    }


}
