package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.utils.Settings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceHolderTest {

  DiceHolder diceHolder;

  @Before
  public void setUp() throws Exception {
    this.diceHolder = new DiceHolder();
  }

  @Test
  public void addDice() {
    // Testing on a number of turn greater than the number of turns
    try{
      this.diceHolder.addDice(Settings.TURNS , new DiceFace(GameColor.PURPLE, 2));
      fail();
    } catch(Exception e){}

    // Testing on a number less than 0
    try{
      this.diceHolder.addDice(-1, new DiceFace(GameColor.PURPLE, 3));
      fail();
    } catch(Exception e){}

  }

  @Test
  public void removeDice() {
  }

  @Test
  public void getTurnDices() {
  }
}