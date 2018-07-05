package it.polimi.se2018.view;

/**
 * An interface to implement the ability of an event to be visited by the View
 */
public interface VisitableFromView {
    public void visit(GameTable gameTable);
    public void visit(GameEnding gameEnding);
    public void visit(SelectSchemaCardFace selectSchemaCardFace);
}
