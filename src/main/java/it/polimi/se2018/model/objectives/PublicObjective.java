package it.polimi.se2018.model.objectives;

/**
 * A generic class to represent Public Objective. They can have different score
 */
public abstract class PublicObjective extends Objective {

    private int points;

    public PublicObjective(int points) {
        this.points = points;
    }

    public int getPoint() {
        return points;
    }

    @Override
    public String toString() {
        return "PublicObjective{" +
            "points=" + points +
            "Class" + this.getClass().getName() +
            '}';
    }
}
