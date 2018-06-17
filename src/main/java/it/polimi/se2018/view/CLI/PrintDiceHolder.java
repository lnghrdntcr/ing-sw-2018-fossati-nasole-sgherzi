package it.polimi.se2018.view.CLI;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model_view.DiceHolderImmutable;

public class PrintDiceHolder {
    public static void main(String args[]) {
        DiceFace[][] dices = new DiceFace[][] {
            new DiceFace[] { new DiceFace(GameColor.RED, 2), new DiceFace(GameColor.BLUE,2)},
            new DiceFace[] { new DiceFace(GameColor.YELLOW, 3), new DiceFace(GameColor.YELLOW,6), new DiceFace(GameColor.YELLOW,4)},
            new DiceFace[] { new DiceFace(GameColor.PURPLE, 2), new DiceFace(GameColor.GREEN,4)},
            new DiceFace[] { new DiceFace(GameColor.RED, 2), new DiceFace(GameColor.BLUE,2)},
            new DiceFace[] { new DiceFace(GameColor.YELLOW, 5), new DiceFace(GameColor.YELLOW,4), new DiceFace(GameColor.YELLOW,4)},
            new DiceFace[] { new DiceFace(GameColor.RED, 5), new DiceFace(GameColor.PURPLE,4)},
            new DiceFace[] { new DiceFace(GameColor.RED, 6), new DiceFace(GameColor.BLUE,6)},
            new DiceFace[] { new DiceFace(GameColor.RED, 2), new DiceFace(GameColor.BLUE,1)},
        };

        DiceHolderImmutable diceHolder = new DiceHolderImmutable(dices);

        CLIPrinter.printDiceHolder(diceHolder);

    }
}
