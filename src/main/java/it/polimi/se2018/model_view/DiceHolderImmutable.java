package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.utils.Settings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DiceHolder to be used in View
 *
 * @since 18/05/2018
 */
public class DiceHolderImmutable implements Serializable {

    private DiceFace[][] turnHolder;

    public DiceHolderImmutable(DiceFace[][] turnHolder) {

        if (turnHolder == null)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": turnHolder Cannot be null.");
        this.turnHolder = turnHolder;

    }

    public static DiceHolderImmutable fromJSON(JSONObject diceHolderImmutable) {
        int doneTurns=diceHolderImmutable.getInt("doneTurns");
        JSONArray turns=diceHolderImmutable.getJSONArray("holder");

        DiceFace[][] turnHolder = new DiceFace[doneTurns][];
        for(int turnino=0; turnino<doneTurns; turnino++){
            JSONArray singleTurn = turns.getJSONArray(turnino);
            turnHolder[turnino] = new DiceFace[singleTurn.length()];

            for(int x=0; x<singleTurn.length(); x++){
                turnHolder[turnino][x]=DiceFace.fromJson(singleTurn.getJSONObject(x));
            }
        }
        return new DiceHolderImmutable(turnHolder);
    }

    public int getDoneTurns() {
        return this.turnHolder.length;
    }

    public DiceFace[] getDiceFaces(int turn) {

        if (turn < 0 || turn >= this.turnHolder.length)
            throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": Trying to access an illegal turn");

        return this.turnHolder[turn];

    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doneTurns", getDoneTurns());

        JSONArray turns =  new JSONArray();
        for(int turnino=0; turnino<turnHolder.length; turnino++){
            JSONArray singleTurn = new JSONArray();
            for(DiceFace df : getDiceFaces(turnino)){
                singleTurn.put(df.toJSON());
            }
            turns.put(singleTurn);
        }

        jsonObject.put("holder", turns);
        return jsonObject;
    }
}
