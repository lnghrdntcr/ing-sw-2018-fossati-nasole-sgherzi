package it.polimi.se2018.model.schema;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceFaceTest {

  @Test
  public void constructorTestOnNumber() {

    DiceFace actualDiceFace;

    // When GameColor is null
    try{
      actualDiceFace = new DiceFace(null, 3);
      fail();
    } catch (Exception e){}

    // When number is a range [0, 6]
    for(int i = 0; i <= 7; i++){
      if(i == 0 || i == 7){
        try{
          actualDiceFace = new DiceFace(GameColor.PURPLE, i);
          fail();
        } catch(Exception e){}
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
  }
}