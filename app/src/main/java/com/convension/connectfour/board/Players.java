package com.convension.connectfour.board;

public class Players
{
  public static final int NONE = 0;
  public static final int PLAYER1 = 1;
  public static  int INDEX=  -1;
  public static  int OPPONNENT_INDEX=  -1;
  public static final int PLAYER2 = -1;
  public static final int POWER_PLAYER = 2;
  public static final int ONE_PLAYER = 0;
  public static final int TWO_PLAYERS = 1;
  public static String FIRST_PLAYER="Player 1";
  public static String SECOND_PLAYER="Player 2";
  public static Boolean IS_SERVER=false;
  public static Boolean IS_MULTIPLAYER_SERVER=false;
  public static final int GO_FIRST = 0;
  public static final int GO_SECOND = 1;
  public static final int DIFF_EASY = 0;
  public static final int DIFF_MED = 1;
  public static final int DIFF_HARD = 2;
  public static final int SOUND_ON = 0;
  public static final int SOUND_OFF = 1;
  public static final int POWER_ON = 0;
  public static final int POWER_OFF = 1;
  public static final int THEME_DEFAULT=1;
  public static void resetAllSettings(){
    INDEX=  -1;
    OPPONNENT_INDEX=  -1;
    FIRST_PLAYER="Player 1";
    SECOND_PLAYER="Player 2";
    IS_SERVER=false;
    IS_MULTIPLAYER_SERVER=false;

  }
}