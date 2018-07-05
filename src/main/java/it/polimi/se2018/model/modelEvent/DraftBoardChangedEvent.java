package it.polimi.se2018.model.modelEvent;

import it.polimi.se2018.model.DraftBoard;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model_view.DraftBoardImmutable;
import it.polimi.se2018.view.GameEnding;
import it.polimi.se2018.view.GameTable;
import it.polimi.se2018.view.SelectSchemaCardFace;
import org.json.JSONObject;

/**
 * Event that is used to inform that the DraftBoard has been changed
 */
public class DraftBoardChangedEvent extends ModelEvent {
    DraftBoardImmutable draftBoardImmutable;

    public DraftBoardChangedEvent(String emitter, String receiver, String player, DraftBoardImmutable draftBoardImmutable) {
        super(emitter, player, receiver);
        this.draftBoardImmutable = draftBoardImmutable;
    }

    public DraftBoardImmutable getDraftBoardImmutable() {
        return draftBoardImmutable;
    }

    public DraftBoardChangedEvent(String json){
        super(json);
        draftBoardImmutable=DraftBoardImmutable.fromJson(new JSONObject(json).getJSONObject("draftBoardImmutable"));
    }

    @Override
    public void visit(GameTable gameTable) {
        gameTable.handleDraftBoardChanged(this);
    }

    @Override
    public void visit(GameEnding gameEnding) {

    }

    @Override
    public void visit(SelectSchemaCardFace selectSchemaCardFace) {

    }


    public static void main(String a[]){
        DraftBoard draftBoard = new DraftBoard();
        draftBoard.drawDices(3);
        System.out.println(new DraftBoardChangedEvent("emitter", "receiver", "player", draftBoard.getImmutableInstance()).toJSON().toString());
    }


}
