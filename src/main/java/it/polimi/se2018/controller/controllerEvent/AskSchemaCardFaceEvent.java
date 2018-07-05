package it.polimi.se2018.controller.controllerEvent;

import it.polimi.se2018.model.schema_card.SchemaCard;
import it.polimi.se2018.model.schema_card.Side;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Event to ask the player to select a SchemaCardFace, at the beginning of the game
 */
public class AskSchemaCardFaceEvent extends ControllerEvent {
    SchemaCard[] schemas;

    public AskSchemaCardFaceEvent(String emitter, String receiver, String player, SchemaCard[] schemas) {
        super(emitter, player, receiver);
        this.schemas = schemas;
    }

    public AskSchemaCardFaceEvent(String json) {
        super(json);
        schemas=new SchemaCard[2];
        JSONObject jsonObject= new JSONObject(json);
        schemas[0]=SchemaCard.loadSchemaCardFromJsonObj(jsonObject.getJSONArray("schemas").getJSONObject(0));
        schemas[1]=SchemaCard.loadSchemaCardFromJsonObj(jsonObject.getJSONArray("schemas").getJSONObject(1));
    }

    public SchemaCard[] getSchemas() {
        return schemas;
    }

    @Override
    public void visit(GameTable gameTable) {

    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {
        selectSchemaCardFace.showSchemaCardFaceSelection(this);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();

        ArrayList<JSONObject> schemasJson = new ArrayList<>();
        Arrays.stream(schemas).forEach(schemaCard -> schemasJson.add(schemaCard.toJsonObj()));

        jsonObject.put("schemas", schemasJson);

        return jsonObject;
    }
}
