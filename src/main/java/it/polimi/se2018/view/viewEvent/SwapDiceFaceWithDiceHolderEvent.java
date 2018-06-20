package it.polimi.se2018.view.viewEvent;

import it.polimi.se2018.controller.states.State;

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