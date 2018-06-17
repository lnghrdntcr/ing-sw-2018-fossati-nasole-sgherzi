package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model_view.ToolCardImmutable;

public class PrintToolcard {
    public static void main(String args[]) {
        CLIPrinter.printToolcard(new ToolCardImmutable("CircularCutter", 3), 0);
    }
}
