package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceBag;
import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.model_view.DiceHolderImmutable;
import it.polimi.se2018.utils.Settings;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DiceHolderTest {

  private DiceHolder diceHolder;
  private DiceBag diceBag;
  private final int DICES_PER_TURN = 3;
  private final int MISSING_TURN = 7;

  @Before
  public void setUp() throws Exception {

    this.diceHolder = new DiceHolder();

    // Adds two diceFace(s) for each turn.
    for(int i = 0; i < Settings.TURNS; i++ ){
      if(i != MISSING_TURN){
        for(int j = 0; j < this.DICES_PER_TURN; j++){
          this.diceHolder.addDice(i, new DiceFace(GameColor.values()[(i + j) % GameColor.values().length], ((i + j) % 6) + 1 ));
        }
      }
    }
  }

  @Test
  public void addDice() {

    // Testing against a null diceFace.
    try{
      this.diceHolder.addDice(1, null);
      fail();
    } catch (IllegalArgumentException e){}

    // Testing against a turn not in range.
    try{
      this.diceHolder.addDice(Settings.TURNS , new DiceFace(GameColor.PURPLE, 2));
      fail();
    } catch(Exception e){}

    // Testing against an illegal turn.
    try{
      this.diceHolder.addDice(-1, new DiceFace(GameColor.PURPLE, 3));
      fail();
    } catch(Exception e){}

    // Testing normal behaviour: Adding dices and checking if that dice is in that position
    for(int i = 0; i < Settings.TURNS; i++){
      if(i != MISSING_TURN){
        for(int j = 0; j < this.DICES_PER_TURN; j++){
          this.diceHolder.addDice(i, new DiceFace(GameColor.values()[(i + j) % GameColor.values().length], ((i + j) % 6) + 1 ));
        }
      }
    }

    for(int i = 0; i < Settings.TURNS; i++){
      if(i != this.MISSING_TURN){
        for(int j = (this.DICES_PER_TURN - 1); j >= 0; j--){
          assertEquals(new DiceFace(GameColor.values()[(i + j) % GameColor.values().length], ((i + j) % 6) + 1 ).getColor(), this.diceHolder.getTurnDices(i)[j].getColor());
          assertEquals(new DiceFace(GameColor.values()[(i + j) % GameColor.values().length], ((i + j) % 6) + 1 ).getNumber(), this.diceHolder.getTurnDices(i)[j].getNumber());
        }
      }
    }

  }

  @Test
  public void removeDice() {

    // Testing against an illegal turn.
    try{
      this.diceHolder.removeDice(-1, this.DICES_PER_TURN - 1);
      fail();
    } catch (Exception e){}

    //Testing against an illegal position.
    try{
      this.diceHolder.removeDice(this.MISSING_TURN - 1, -1);
      fail();
    } catch (Exception e){}

    // Testing against a turn not in range.
    try {
      this.diceHolder.removeDice(Settings.TURNS, this.DICES_PER_TURN - 1);
      fail();
    } catch (Exception e){}

    // Testing against a position not in range.
    try{
      this.diceHolder.removeDice(this.MISSING_TURN - 1, this.DICES_PER_TURN);
      fail();
    } catch (Exception e){}

    // Testing against a non existing turn.
    try {
      this.diceHolder.removeDice(this.MISSING_TURN, this.DICES_PER_TURN - 1);
      fail();
    } catch (Exception e){}

    // Testing normal behaviour: trying to remove every dice from each turn.
    for (int i = 0; i < Settings.TURNS; i++){
      if(i != this.MISSING_TURN){
        for (int j = (this.DICES_PER_TURN - 1); j >= 0; j--){
          this.diceHolder.removeDice(i, j);
        }
      }
    }

  }

  @Test
  public void getTurnDices() {

    // Testing against an illegal turn.
    try {
      this.diceHolder.getTurnDices(-1);
      fail();
    } catch (IllegalArgumentException e){}

    // Testing against a turn not in range.
    try{
      this.diceHolder.getTurnDices(Settings.TURNS );
      fail();
    } catch(IllegalArgumentException e){}

    // Testing normal behaviour: get the turn dices for each turn
    for(int i = 0; i < Settings.TURNS; i++){
      if(i == this.MISSING_TURN){
        try{
          this.diceHolder.getTurnDices(i);
        } catch (IllegalArgumentException e){}
      } else{
        this.diceHolder.getTurnDices(i);
      }
    }

  }

  @Test
  public void getImmutableInstance() {

    DiceHolderImmutable immutableDiceHolder = this.diceHolder.getImmutableInstance();

    for (int i = 0; i < Settings.TURNS; i++) {
      if(i != MISSING_TURN) assertTrue(Arrays.equals(immutableDiceHolder.getDiceFaces(i), this.diceHolder.getTurnDices(i)));
    }


  }

  @Test
  public void isColorPresent() {

    // A new DiceHolder with all red dices in order to test the false result of this function.
    DiceHolder actualDiceHolder = new DiceHolder();

    for (int i = 0; i < Settings.TURNS; i++) {
      actualDiceHolder.addDice(i, new DiceFace(GameColor.RED, 1));
    }

    for(GameColor gc: GameColor.values()){
      assertTrue(this.diceHolder.isColorPresent(gc));
    }

    // As the `actualDiceHolder` was created only to have RED dices.
    assertFalse(actualDiceHolder.isColorPresent(GameColor.BLUE));

  }
}