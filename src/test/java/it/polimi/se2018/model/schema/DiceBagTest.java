package it.polimi.se2018.model.schema;

import static org.junit.Assert.*;

import it.polimi.se2018.utils.Settings;
import org.junit.*;

import java.util.ArrayList;
import java.util.EnumMap;


public class DiceBagTest {

  private DiceBag diceBag;

  @Before
  public void setUp() throws Exception {
    this.diceBag = new DiceBag();
  }

  @Test
  public void drawDice() {

    EnumMap<GameColor, Integer> counter = new EnumMap<GameColor, Integer>(GameColor.class);
    DiceFace actualDiceFace;

    // Testing normal behaviour
    for(GameColor gc: GameColor.values()){
      counter.put(gc, 0);
    }

    for(int i = 0; i < Settings.MAX_DICE_PER_COLOR * GameColor.values().length; i++){
      actualDiceFace = this.diceBag.drawDice();
      counter.put(actualDiceFace.getColor(), counter.get(actualDiceFace.getColor()) + 1);
    }

    for(GameColor gc: GameColor.values()){
      assertEquals(Settings.MAX_DICE_PER_COLOR, counter.get(gc).intValue());
    }

    //Trying to draw the 91th dice
    try{
      this.diceBag.drawDice();
      fail();
    } catch(IllegalStateException e){}

  }

  @Test
  public void putBackDice() {

    try{
      this.diceBag.putBackDice(null);
      fail();
    } catch(IllegalArgumentException e){}


    for(int i = 0; i < Settings.MAX_DICE_PER_COLOR * GameColor.values().length; i++){
      this.diceBag.drawDice();
    }

    /*
     * Testing normal behaviour:
     * Attempting to put back all dices of a color from the diceBag.
     */
    for(GameColor gc: GameColor.values()){
      for(int i = 0; i < Settings.MAX_DICE_PER_COLOR; i++) {
        // Using new DiceFace(gc, 2) because the number is actually useless.
        this.diceBag.putBackDice(new DiceFace(gc, 2));
      }
    }

    // Trying to put back one more, of each color
    for(GameColor gc: GameColor.values()){
      try{
        this.diceBag.putBackDice(new DiceFace(gc, 2));
        fail();
      } catch (IllegalStateException e){}
    }

  }
}