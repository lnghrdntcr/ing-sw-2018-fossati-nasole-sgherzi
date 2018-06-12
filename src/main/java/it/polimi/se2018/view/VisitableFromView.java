package it.polimi.se2018.view;

public interface VisitableFromView {
    public void visit(GameTable gameTable);
    public void visit(GameEnding gameEnding);
    public void visit(SelectScheaCardFace selectScheaCardFace);
}
