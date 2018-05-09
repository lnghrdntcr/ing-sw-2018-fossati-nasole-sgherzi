package it.polimi.se2018.model.schema;

import com.sun.istack.internal.NotNull;
import it.polimi.se2018.utils.Settings;

import java.util.EnumMap;
import java.util.Random;

/**
 * Creates DiceFaces and holds them.
 * It also checks that not more than a certain number of dice is created for each color.
 * @author Francesco Sgherzi
 * @since 09/05/2018
 */
public class DiceBag {
    // TODO: Implement Tests
    private Random random;
    private EnumMap<GameColor, Integer> counter = new EnumMap<>(GameColor.class);
    private int dicesDrawn;

    public DiceBag(){
        for(GameColor gc: GameColor.values()){
            counter.put(gc, 0);
        }
        random = new Random();
        dicesDrawn = 0;
    }

    /**
     * Draws a dice checking that the number of dices drawn per color is less than `Settings.MAX_DICE_PER_COLOR`
     * and the number of total drawn dices is less than `Settings.MAX_DICE_PER_COLOR * GameColor.values().length`
     * @return A new DiceFace, according to internal checks.
     * @throws IllegalStateException if the number of dices drawn is greater than 90.
     */
    public DiceFace drawDice() {

        if(dicesDrawn >= Settings.MAX_DICE_PER_COLOR * GameColor.values().length) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Attempting to draw one more dice, but already drawn 90");

        int color = random.nextInt(GameColor.values().length);
        int number = random.nextInt(6) + 1;
        GameColor[] values = GameColor.values();

        while(counter.get(values[color]) >= Settings.MAX_DICE_PER_COLOR){
            color = random.nextInt(GameColor.values().length);
        }

        counter.put(values[color], counter.get(values[color]) + 1);
        dicesDrawn += 1;

        return new DiceFace(GameColor.values()[color], number);

    }

    /**
     * Puts back a dice, checking that a dice of that color has ever been drawn, or if any dice has ever been drawn.
     * @param diceFace The DiceFace to be put back.
     * @throws IllegalStateException If the number of dices drawn is less than 0.
     */
    public void putBackDice(@NotNull DiceFace diceFace) {

        if(diceFace == null) throw new IllegalArgumentException(this.getClass().getCanonicalName() + ": diceFace cannot be null.");

        GameColor actualColor = diceFace.getColor();

        if(counter.get(actualColor) <= 0) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Attempting to put back a dice, but no dice of that color has ever been drawn.");
        if(dicesDrawn <= 0) throw new IllegalStateException(this.getClass().getCanonicalName() + ": Attempting to put back a dice, but no dice has ever been drawn.");

        counter.put(actualColor, counter.get(actualColor) - 1);
        dicesDrawn -= 1;

    }
}
