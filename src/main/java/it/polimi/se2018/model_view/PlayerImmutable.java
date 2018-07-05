package it.polimi.se2018.model_view;

import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.Schema;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Player to be used in View
 *
 * @since 18/05/2018
 */
public class PlayerImmutable implements Serializable, Cloneable {

    private String name;
    private int token;
    private PrivateObjective privateObjective;

    public PlayerImmutable(String name, int token, PrivateObjective privateObjective) {

        this.name = name;
        this.token = token;
        this.privateObjective = privateObjective;

    }

    public static PlayerImmutable fromJSON(JSONObject jsonObject) {
        return new PlayerImmutable(jsonObject.getString("name"), jsonObject.getInt("token"), PrivateObjective.fromJSON(jsonObject.getJSONObject("privateObjective")));
    }

    public String getName() {
        return name;
    }


    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    public int getToken() {
        return token;
    }

    public PlayerImmutable obtainFilteredInstance(){
        PlayerImmutable toReturn=null;
        try {
            toReturn = (PlayerImmutable) clone();
            toReturn.privateObjective=null;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return toReturn;

    }

}
