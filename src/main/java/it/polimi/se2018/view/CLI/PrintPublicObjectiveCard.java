package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.objectives.ColorVariety;
import it.polimi.se2018.model.objectives.ColoredDiagonals;

public class PrintPublicObjectiveCard {
    public static void main(String args[]) {
        CLIPrinter.printPublicObjectives(new ColorVariety());
    }
}
