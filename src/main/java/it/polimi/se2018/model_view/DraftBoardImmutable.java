package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * DraftBoard to be used in View
 *
 * @since 18/05/2018
 */
public class DraftBoardImmutable implements Serializable {

    private DiceFace[] drawnDices;

    public DraftBoardImmutable(DiceFace[] drawnDices) {
        this.drawnDices = drawnDices;
    }

    public static DraftBoardImmutable fromJson(JSONObject draftBoardImmutable) {
        JSONArray dices = draftBoardImmutable.getJSONArray("dices");
        DiceFace[] drawnDices = new DiceFace[dices.length()];

        for(int i=0; i<dices.length(); i++){
            drawnDices[i]=DiceFace.fromJson(dices.getJSONObject(i));
        }

        return new DraftBoardImmutable(drawnDices);
    }

    public DiceFace[] getDices() {
        return drawnDices;
    }
}
