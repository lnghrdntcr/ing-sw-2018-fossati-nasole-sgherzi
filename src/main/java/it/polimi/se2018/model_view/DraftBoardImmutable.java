package it.polimi.se2018.model_view;

import it.polimi.se2018.model.schema.DiceFace;

/**
 * DraftBoard to be used in View
 * @since 18/05/2018
 */
public class DraftBoardImmutable {

  private DiceFace[] drawnDices;

  public DraftBoardImmutable(DiceFace[] drawnDices){
    this.drawnDices = drawnDices;
  }

  public DiceFace[] getDices() {
    return drawnDices;
  }
}
