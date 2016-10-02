package com.convension.connectfour.algorithm;

import com.convension.connectfour.board.APoint;
import com.convension.connectfour.board.IBoard;

public abstract class AAlgorithm
{
  protected IBoard b;
  
  public AAlgorithm(IBoard paramIBoard)
  {
    this.b = paramIBoard;
  }
  
  public abstract APoint getBestPlay(int paramInt);
}


/* Location:              C:\Users\Hassan\Desktop\connectfourjar\connect4Lib.jar!\com\jgrindall\android\connect4\lib\algorithm\AAlgorithm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */