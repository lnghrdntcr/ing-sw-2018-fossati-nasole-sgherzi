package it.polimi.se2018.model.schema;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DiceFaceTest {

  @Test
  public void constructorTestOnNumber() {

    DiceFace actualDiceFace;

    // When GameColor is null
    try{
      actualDiceFace = new DiceFace(null, 3);
      fail();
    } catch (IllegalArgumentException e){}

    // When number is a range [0, 6]
    for(int i = 0; i <= 7; i++){
      if(i == 0 || i == 7){
        try{
          actualDiceFace = new DiceFace(GameColor.PURPLE, i);
          fail();
        } catch(IllegalArgumentException e){}
      } else {
        actualDiceFace = new DiceFace(GameColor.PURPLE, i);
        assertEquals(i, actualDiceFace.getNumber());
      }
    }

  }

  @Test
  public void constructorTestOnColor(){
    for(GameColor gc: GameColor.values()){
      assertEquals(gc, new DiceFace(gc, 4).getColor() );
    }
  }

  @Test
  public void isSimilar() {

    DiceFace actualDiceFace = new DiceFace(GameColor.PURPLE, 3);
    ArrayList<DiceFace> diceFaces = new ArrayList<>();

    // Testing against a null input
    try{
      actualDiceFace.isSimilar(null);
      fail();
    } catch(IllegalArgumentException e){}

    for(GameColor gc: GameColor.values()){
      for(int i = 1; i <= 6; i++){
        diceFaces.add(new DiceFace(gc, i));
      }
    }

    for(GameColor gc: GameColor.values()){
      for(int i = 1; i <= 6; i++){
        actualDiceFace = new DiceFace(gc, i);
        for(int j = 0; j < GameColor.values().length * 6; j++){
          if(actualDiceFace.getColor() == diceFaces.get(j).getColor() || actualDiceFace.getNumber() == diceFaces.get(j).getNumber()){
            assertTrue(actualDiceFace.isSimilar(diceFaces.get(j)));
          } else {
            assertFalse(actualDiceFace.isSimilar(diceFaces.get(j)));
          }
        }
      }
    }

  }
}