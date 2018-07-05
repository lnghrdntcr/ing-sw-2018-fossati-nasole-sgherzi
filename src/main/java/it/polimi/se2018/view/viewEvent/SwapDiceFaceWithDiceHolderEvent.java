package it.polimi.se2018.view.viewEvent;

import org.json.JSONObject;

public class SwapDiceFaceWithDiceHolderEvent extends UseToolcardEvent {
    private final int draftBoardIndex;
    private final int turn;
    private final int indexInTurn;

    public SwapDiceFaceWithDiceHolderEvent(String emitter, String receiver, String player, int toolCardIndex, int draftBoardIndex, int turn, int indexInTurn) {
        super(emitter, receiver, player, toolCardIndex);
        this.draftBoardIndex = draftBoardIndex;
        this.turn = turn;
        this.indexInTurn = indexInTurn;
    }

    public SwapDiceFaceWithDiceHolderEvent(String json){
        super(json);
        JSONObject jsonObject = new JSONObject(json);
        draftBoardIndex=jsonObject.getInt("draftBoardIndex");
        turn=jsonObject.getInt("turn");
        indexInTurn=jsonObject.getInt("indexInTurn");
    }

    public int getDraftBoardIndex() {
        return draftBoardIndex;
    }

    public int getTurn() {
        return turn;
    }

    public int getIndexInTurn() {
        return indexInTurn;
    }


}
