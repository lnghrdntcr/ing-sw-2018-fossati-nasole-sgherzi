package it.polimi.se2018.model;

import it.polimi.se2018.model.schema.DiceFace;
import it.polimi.se2018.model.schema.GameColor;
import it.polimi.se2018.utils.Settings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DraftBoardTest {

  DraftBoard draftBoard;

  @Before
  public void setUp() throws Exception {
    this.draftBoard = new DraftBoard();
  }

  @Test
  public void drawDices() {

    int expectedDicesNeeded;
    DraftBoard actualDraftBoard = new DraftBoard();

    // Testing against an illegal number of players: [-2, 2) U (4, 10) .
    for(int i = -2; i < Settings.MIN_NUM_PLAYERS; i++ ){
      try{
        actualDraftBoard.drawDices(i);
        fail();
      } catch (IllegalArgumentException e){}
    }

    for(int i = (Settings.MAX_NUM_PLAYERS + 1); i < 10; i++){
      try{
        actualDraftBoard.drawDices(i);
        fail();
      } catch (IllegalArgumentException e){}
    }

    // Testing normal behaviour. Drawing the dice needed by `i` players.
    for(int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++ ){

      actualDraftBoard = new DraftBoard();
      expectedDicesNeeded = 2 * i + 1;

      actualDraftBoard.drawDices(i);
      assertEquals(expectedDicesNeeded, actualDraftBoard.getDiceNumber());
    }

  }

  @Test
  public void removeDice() {

    DraftBoard actualDraftBoard = new DraftBoard();
    // Trying to remove a dice from an illegal (negative) position.
    try{
      actualDraftBoard.removeDice(-1);
      fail();
    } catch(IllegalArgumentException e){}

    // Trying to remove a dice from a position that has not been created.
    try{
      actualDraftBoard.removeDice(1);
      fail();
    } catch (IllegalArgumentException e){}

    // Trying to remove a dice from position 0 when the draftBoard has size 0 (A condition that model.DraftBoard:55 couldn't cover).
    try {
      actualDraftBoard.removeDice(0);
      fail();
    } catch( IllegalStateException e){}

    // Testing normal behaviour: Drawing the amount of dices needed by 2, 3, 4 players and than removing it all.
    for(int i = Settings.MIN_NUM_PLAYERS; i <= Settings.MAX_NUM_PLAYERS; i++){
      actualDraftBoard = new DraftBoard();
      actualDraftBoard.drawDices(i);
      for(int j = (actualDraftBoard.getDiceNumber() - 1); j >= 0; j--){
        actualDraftBoard.removeDice(j);
      }
    }

  }

  @Test
  public void addDice() {

    GameColor[] gcValues = GameColor.values();
    int j = 0; // Use an external index to access values of the draftBoard in the third for loop.

    // Testing against a null input.
    try{
      this.draftBoard.addDice(null);
      fail();
    } catch (IllegalArgumentException e){}

    // Testing normal behaviour: adding all possible combinations of diceFace (gameColor, number) and than checking that they have been added in order.
    for(GameColor gc: gcValues){
      for (int i = 0; i < Settings.MAX_DICE_PER_COLOR; i++) {
        this.draftBoard.addDice(new DiceFace(gc, (i % 6) + 1));
      }
    }
    for(GameColor gc: gcValues){
      for(int i = 0; i < Settings.MAX_DICE_PER_COLOR && j < (Settings.MAX_DICE_PER_COLOR * gcValues.length) - 1; i++){
        assertEquals(new DiceFace(gc, (i % 6) + 1).getColor(), this.draftBoard.getDices()[j].getColor());
        assertEquals(new DiceFace(gc, (i % 6) + 1).getNumber(), this.draftBoard.getDices()[j].getNumber());
        j++;
      }
    }
  }

  @Test
  public void getDices() {

    DiceFace[] allDices;
    GameColor[] gcValues = GameColor.values();
    int j = 0; // Use an external index to access values of the draftBoard in the third for loop.

    // Trying to get the dices, but no dice has still been drawn.
    try{
      this.draftBoard.getDices();
      fail();
    } catch (IllegalStateException e){}

    // Testing normal behaviour: adding all possible combinations of diceFace (gameColor, number) and than checking that they have been returned in order.
    for(GameColor gc: gcValues){
      for (int i = 0; i < Settings.MAX_DICE_PER_COLOR; i++) {
        this.draftBoard.addDice(new DiceFace(gc, (i % 6) + 1));
      }
    }

    allDices = this.draftBoard.getDices();

    for(GameColor gc: gcValues){
      for (int i = 0; i < Settings.MAX_DICE_PER_COLOR; i++) {
        assertEquals(new DiceFace(gc, (i % 6) + 1).getNumber(), allDices[j].getNumber());
        assertEquals(new DiceFace(gc, (i % 6) + 1).getColor(), allDices[j].getColor());
        j++;
      }
    }

  }

  @Test
  public void getDiceNumber() {

    int j = 0; // Use an external index to access values of the draftBoard in the third for loop.


    // Testing on an empty `diceFaces`.
    assertEquals(0, this.draftBoard.getDiceNumber());

    for( GameColor gc: GameColor.values()){
      for (int i = 0; i < Settings.MAX_DICE_PER_COLOR; i++) {
        this.draftBoard.addDice(new DiceFace(gc, (i % 6) + 1));
        j++;
        assertEquals(j, this.draftBoard.getDiceNumber());
      }
    }
  }
}