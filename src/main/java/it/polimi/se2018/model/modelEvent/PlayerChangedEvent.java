package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.objectives.PrivateObjective;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model_view.PlayerImmutable;
import it.polimi.se2018.utils.Event;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

/**
 * Event that is used to inform that a payer has been changed
 */
public class PlayerChangedEvent extends ModelEvent {
    private final PlayerImmutable playerImmutable;

    public PlayerChangedEvent(String emitter, String receiver, String player, PlayerImmutable playerImmutable) {
        super(emitter, player, receiver);
        this.playerImmutable = playerImmutable;
    }

    public PlayerChangedEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        playerImmutable=PlayerImmutable.fromJSON(jsonObject.getJSONObject("playerImmutable"));
    }

    public PlayerImmutable getPlayerImmutable() {
        return playerImmutable;
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handlePlayerChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {
        selectSchemaCardFace.handlePlayerCanged(this);
    }

    @Override
    public Event filter(String playername) {
        if(!playername.equals(playerImmutable.getName())){
            return new PlayerChangedEvent(getEmitterName(), getReceiver(), getPlayerName(), playerImmutable.obtainFilteredInstance());
        }
        return this;
    }

    @Override
    public JSONObject toJSON() {
        return super.toJSON();
    }

    public static void main(String a[]){
        Player player=new Player("player12345");
        player.setPrivateObjective(new PrivateObjective(GameColor.RED));
        player.setToken(7);
        System.out.println(new PlayerChangedEvent("emitter", "receiver", "player",player.getImmutableInstance()).toJSON().toString());

        System.out.println(new PlayerChangedEvent(new PlayerChangedEvent("emitter", "receiver", "player",player.getImmutableInstance()).toJSON().toString()).toJSON().toString());
    }
}
