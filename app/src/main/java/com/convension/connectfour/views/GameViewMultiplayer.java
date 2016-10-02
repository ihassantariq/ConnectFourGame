package com.convension.connectfour.views;

import java.util.*;

import  com.convension.connectfour.Connect4App;
import  com.convension.connectfour.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;

import  com.convension.connectfour.inter.*;
import com.convension.connectfour.board.*;
import android.content.res.Resources;
import android.graphics.*;
import android.media.MediaPlayer;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import  com.convension.connectfour.utils.*;

public class GameViewMultiplayer extends FrameLayout implements View.OnTouchListener, IOnUndoListener, IOnNewGameListener, IWinListener{

    private IGameViewListener mMessageSendListener;
    private RelativeLayout piecesFrame;
    private WinLinesView winLinesFrame;
    private TextView mTimerTextView,mPlayerTextView;
    private LayoutInflater lInf;
    private ImageView boardImage;
    private IOnDebugListener dList;
    private IOnExitListener exList;
    private IOnTurnChangeListener turnList;
    private GameBoard gameBoard = new GameBoard();
    private Resources res;
    private int columnPlayed;
    private View newPiece;
    private Stack<Point> moveStack = new Stack<Point>();
    private int whoWon;
    private IBoard board = new Board();
    private SharedPreferences settings = this.getContext().getApplicationContext().getSharedPreferences(Connect4App.PREFS_NAME, 0);
    protected int numPlayers;
    private CompTask processTask;
    private boolean boardEnabled;
    private PowerBall powerBall = new PowerBall();
    private boolean mActivateBoard;
    private CountDownTimer mTimer;

    private TopView mtopView;
    private Context mContext;

    public GameViewMultiplayer(Context c, AttributeSet a){
        super(c,a);
        mContext=c;
        mActivateBoard=Players.IS_SERVER;
        init();
    }
    public GameViewMultiplayer(Context c){
        super(c);
        mContext=c;
        init();
    }
    public GameViewMultiplayer(Context c, AttributeSet a, int ds){
        super(c,a,ds);
        mContext=c;
        init();
    }
    public void setDepth(int d){
        board.setDepth(d);
    }
    public void setNumPlayers(int p){
        numPlayers = p;
    }
    public void setDifficulty(int d){
        board.setDifficulty(d);
    }
    public void setOnTurnChangeListener(IOnTurnChangeListener list){
        turnList = list;
    }
    public void setOnExitListener(IOnExitListener list){
        exList = list;
    }
    private int getPieceId(){
        if(board.getPlayersGo()==Players.PLAYER1){
            return R.layout.firstpiece;
        }
        else{
            return R.layout.secondpiece;
        }
    }
    private void addPiece(int i){
        boolean p = powerBall.playNow();
        MarginLayoutParams mp = new MarginLayoutParams(gameBoard.getPieceDiam(),gameBoard.getPieceDiam());

        newPiece = lInf.inflate(getPieceId(), null);
        mp.leftMargin  =  gameBoard.getX(i);
        mp.topMargin =    gameBoard.getY(0);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mp);
        //ViewGroup viewGroup=(ViewGroup)newPiece;
        ImageView imageView= (ImageView )newPiece;
        int id=0;
         id = getPieceId ();
        Util.setImageViewDrawable (imageView,id,getContext ());
        piecesFrame.addView(newPiece, params);
        if(p){
            powerBall.setHasBeenPlayed(true);
            powerBall.setJustPlayed( true );
        }
    }
    public void init(){
        this.setWillNotDraw(false);
        res = this.getContext().getApplicationContext().getResources();
        lInf = (LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        lInf.inflate(R.layout.multiplayer_game, this, true);
        setTimer ();
    }
    public void inflated(){
        newGame();
    }
    private void checkInitiated(){
        if(boardImage==null || gameBoard.getX(0)==0){
            initBoard();
        }
        this.invalidate();
    }
    public void newGame(){
        checkInitiated();
        whoWon = Players.NONE;
        board.reset();
        int p = settings.getInt(Connect4App.PREFS_PLAY, Players.ONE_PLAYER);
        powerBall.setInUse(  (settings.getInt(Connect4App.PREFS_POWER, Players.POWER_OFF) == Players.POWER_ON)  );
        this.setNumPlayers(1);
        this.debug("");
        changeTop();
        powerBall.reset();
        moveStack.clear();
        this.enableBoard(true);
        if(piecesFrame!=null){
            piecesFrame.removeAllViews();
        }
        if(winLinesFrame!=null){
            winLinesFrame.reset();
        }
    }
    private void initBoard(){
        FrameLayout f = (FrameLayout)(this.findViewById (R.id.main_layout));
        boardImage = (ImageView)f.findViewById(R.id.board);
        piecesFrame = (RelativeLayout)f.findViewById(R.id.piecesframe);
        winLinesFrame = (WinLinesView)f.findViewById(R.id.winlinesframe);
        mTimerTextView = (TextView)this.findViewById(R.id.timer_text_view);
        mPlayerTextView = (TextView)this.findViewById(R.id.turn_text_view);

        gameBoard.setBoardDimensions(this.findViewById (R.id.main_layout).getWidth (), this.findViewById (R.id.main_layout).getHeight () );
        winLinesFrame.setPieceDiam(gameBoard.getPieceDiam());
        winLinesFrame.setWinListener(this);
    }
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if(mActivateBoard) {
            //get x,y and play piece
            checkInitiated ();
            if ( e.getAction () == MotionEvent.ACTION_DOWN ) {
                float xPos = e.getX ();
                int colNum = gameBoard.getColForTouch (xPos);
                if ( !board.colFull (colNum) ) {
                    enableBoard (false);
                    addPiece (colNum);
                    columnPlayed = colNum;
                    move ();
                    mActivateBoard=false;
                    mMessageSendListener.onRealTimeMessageSend (colNum,false);

                }
            }
        }
        return false;
    }
    public void setTimer(){

        stopTimer();
        mTimer=new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(mTimerTextView!=null) {
                    mTimerTextView.setText ("" + millisUntilFinished / 1000);
                }
            }

            public void onFinish() {
                if(mTimerTextView!=null) {
                    if(mActivateBoard) {
                        computerGo();
                        mActivateBoard=false;
                    }
                }
                //mTextField.setText("done!");
            }
        }.start();
    }
    public void stopTimer(){

        if(mTimer!=null)
        {
            mTimer.cancel ();
        }
    }
    private void debug(String s){
        if(dList!=null){
            dList.debug(s);
        }
    }
    public void setOnDebugListener(IOnDebugListener l){
        dList = l;
    }
    public boolean getBoardEnabled(){
        return boardEnabled;
    }
    public void enableBoard(boolean tf){
        boardEnabled = tf;
        if(tf){
            this.setOnTouchListener(this);
        }
        else{
            this.setOnTouchListener(null);
        }
    }
    private void computerGo(){
        debug("comp task "+board.getNumSpaces());
        APoint point=board.getBestPlay ();
        mMessageSendListener.onRealTimeMessageSend (point.x,false);
        processTask = new CompTask (point);
        processTask.execute ();
    }
    public boolean cleanUp(){
        try{
            processTask.cancel(true);
            return true;
    }
        catch(Exception e){

        }
        return false;
    }
   /* public void restart(){
        computerGo();
    }*/
    //drop function is basically doing everything from dropping to the next player action and setting the alternative
    private void dropped(){
        int numSteps = board.getStepsDown(columnPlayed);
        board.pushCol(columnPlayed, powerBall.getJustPlayed());
        moveStack.add(new Point(columnPlayed, numSteps));
        APoint[] wonOther = board.checkWin();
        board.alternateTurn();
        APoint[] wonPlayed = board.checkWin();
        if(wonPlayed!=null && wonOther!=null){
            // both win (using powerball)
            whoWon = Players.POWER_PLAYER;
        //    drawTwoWinLines(wonPlayed, wonOther);
            board.alternateTurn();
            return;
        }
        else if(wonPlayed!=null){
            whoWon = board.getPlayersGo();
            drawOneWinLine(wonPlayed);
            board.alternateTurn();
            return;
        }
        else if(wonOther!=null){
            board.alternateTurn();
            whoWon = board.getPlayersGo();
            board.alternateTurn();
            drawOneWinLine(wonOther);
            board.alternateTurn();
            return;
        }
        powerBall.setJustPlayed( false );
        board.alternateTurn();
        if(board.getNumSpaces()==0){
            openDialog(res.getString(R.string.msg_draw));
            return;
        }
        if(numPlayers==Players.TWO_PLAYERS){
            enableBoard(true);
            //here I had to send message. to another player.
        }
        else{
            if(board.getPlayersGo()==Players.PLAYER2){
             //   computerGo();
            }
            else if(board.getPlayersGo()==Players.PLAYER1){
                enableBoard(true);
            }
        }
        changeTop();
    }
    private void changeTop(){
        setTimer ();
        if(numPlayers==Players.TWO_PLAYERS) {
            if ( board.getPlayersGo () == Players.PLAYER1 ) {
                mPlayerTextView.setText (mtopView.getT1().getText());
            } else if ( board.getPlayersGo () == Players.PLAYER2 ) {
                mPlayerTextView.setText (mtopView.getT2().getText());

            }

        }
        if(turnList!=null){
            turnList.onChange(this, board.getPlayersGo());
        }
    }
    public void onWinFinished(){
        String s;
        if(numPlayers==Players.TWO_PLAYERS){
            if(whoWon==Players.PLAYER1){
                if(Players.IS_SERVER) {
                    s = "Congrats, you" +" won!";
                }else{
                    s = "Sorry, you" +" lost!";
                }

            }
            else if(whoWon==Players.PLAYER2){
                if(Players.IS_SERVER) {
                    s = "Sorry, you" +" lost!";
                }else{
                    s = "Congrats, you" +" won!";
                }
            }
            else{
                s = res.getString ( R.string.msg_draw);
            }
        }
        else{
            if(whoWon==Players.POWER_PLAYER){
                s = res.getString ( R.string.msg_draw);
            }
            else if(whoWon==Players.PLAYER1){
                s = res.getString(R.string.msg_youwin);
            }
            else{
                s = res.getString(R.string.msg_youlose);
            }
        }
        openDialog(s);
    }
    private APoint[][] convertToXY(APoint[][] w){
        APoint[][] out = new APoint[w.length][w[0].length];
        for(int i=0;i<=w.length-1;i++){
            APoint[] line = w[i];
            for(int j=0;j<=line.length-1;j++){
                out[i][j] = new APoint(gameBoard.getX(line[j].x), gameBoard.getY(line[j].y) );
            }
        }
        return out;
    }
    private void drawOneWinLine(APoint[] won){

        if(whoWon==Players.PLAYER1){
            if(Players.IS_SERVER) {
                playSuccessSound();
            }else{
                playFailSound();
            }

        }
        else if(whoWon==Players.PLAYER2){
            if(Players.IS_SERVER) {
                playFailSound();
            }else{
                playSuccessSound();
            }
        }
        APoint[][] line = {{won[0]}, {won[1]}, {won[2]}, {won[3]}};
        winLinesFrame.draw(convertToXY(line));
    }
    private void move(){
        int dx = 0;
        final View thisView = this;
        int numSteps = board.getStepsDown(columnPlayed);
        int dy = (int)(numSteps*gameBoard.getRealGapY());
        TranslateAnimation slide = new TranslateAnimation(0,dx,0,dy);
        slide.setInterpolator(new BounceInterpolator());
        slide.setDuration(600);
        slide.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationStart(Animation anim){

            }
            public void onAnimationRepeat(Animation anim){

            }
            public void onAnimationEnd(Animation anim){
                thisView.postInvalidate();
                dropped();
            }
        });
        slide.setFillAfter(true);
        newPiece.startAnimation(slide);
        if(powerBall.getJustPlayed()){
            playDropPowerSound();
        }
        else{
            playDropSound();
        }
    }
    private void playSuccessSound(){
        playSound(R.raw.chime1);
    }
    private void playFailSound(){
        playSound(R.raw.chime2);
    }
    private void playDropSound(){
        playSound(R.raw.drop);
    }
    private void playDropPowerSound(){
        playSound(R.raw.droppower);
    }
    private void playSound(int i){
        SharedPreferences settings = this.getContext().getSharedPreferences(Connect4App.PREFS_NAME, 0);
        int s = settings.getInt(Connect4App.PREFS_SOUND, Players.SOUND_ON);
        if(s==Players.SOUND_OFF){
            return;
        }
        MediaPlayer mp = MediaPlayer.create(this.getContext(), i);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    private void openDialog(String s){
        stopTimer();
        final Dialog dialog = new Dialog(this.getContext(),R.style.MyDialog);
        dialog.setContentView(R.layout.gameover);
        TextView tV = (TextView)dialog.findViewById(R.id.gameover_msg);
        tV.setText(s);
        dialog.setCancelable(false);
        final GameViewMultiplayer gV = this;
        Button undoButton = (Button) dialog.findViewById(R.id.over_undo);
        undoButton.setVisibility (View.GONE);

        Button startOverButton = (Button) dialog.findViewById(R.id.over_restart);

        startOverButton.setText ("Invite Again");
        startOverButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //newGame();
          //      enableBottomButtons(true);
           //     mTimer.cancel();
                mMessageSendListener.onRealTimeMessageSend (0,true);
                dialog.dismiss();
            }
        });

        Button quite = (Button) dialog.findViewById(R.id.over_close);
        quite.setText ("Quite");
        quite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             //   mTimer.cancel();
                mMessageSendListener.onRealTimeMessageSend (1,true);

                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.over_newgame)).setVisibility (View.GONE);
        dialog.show();

    }
    private void exitGame(){
        exList.exit();
    }
    @Override
    public boolean onNewGame(View v) {
        final boolean wasEnabled = this.getBoardEnabled();
        this.enableBoard(false);
        final Dialog dialog = new Dialog(this.getContext(),R.style.MyDialog);
        dialog.setContentView(R.layout.restart);
        TextView tV = (TextView)dialog.findViewById(R.id.restart_msg);
        tV.setText(R.string.surerestart);
        dialog.setCancelable(true);


        ((Button) dialog.findViewById(R.id.restart_ok)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                newGame();
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.restart_cancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                enableBoard(wasEnabled);
                dialog.dismiss();
            }
        });
        dialog.show();

        return false;
    }
    @Override
    public boolean onUndo(View v) {
        //undo();
        return false;
    }

    private void playComputer(APoint p){
        processTask = null;
        debug("played at "+p.x+" type "+PlayTypes.getString(p.y)+" "+board.getNumSpaces());
        columnPlayed = p.x;
        addPiece(columnPlayed);
        move();
    }
    private class CompTask extends AsyncTask<Void, Void, APoint> {

        private  APoint c;
        public  CompTask(APoint c){
            this.c=c;
        }
        @Override
        protected APoint doInBackground(Void... v) {
            return c;
        }
        @Override
        protected void onPostExecute(APoint result) {
            try{
                playComputer(result);
            }
            catch(Exception e){

            }
        }

    }
    private void drawGrid(Canvas c){
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.WHITE);
        for(int i=0;i<=Board.NUMX-1;i++){
            for(int j=0;j<=Board.NUMY-1;j++){
                int tlx = gameBoard.getX(i);
                int tly = gameBoard.getY(j);
                int r = (int)(gameBoard.getPieceDiam()/2);
                c.drawCircle(tlx+r, tly+r, r-4, paint);
            }
        }
    }
    @Override
    protected void onDraw(Canvas c){
        super.onDraw(c);
        if(Connect4App.DEBUG){
            drawGrid(c);
        }

    }
    public void setmMessageSendListener (IGameViewListener mMessageSendListener) {
        this.mMessageSendListener = mMessageSendListener;
    }
    public void onMessageReceived(int colNum,Boolean isFinal) {
        addPiece (colNum);
        columnPlayed = colNum;
        move ();
        mActivateBoard=true;
    }
    public TopView getMtopView() {
        return mtopView;
    }

    public void setMtopView(TopView mtopView) {
        this.mtopView = mtopView;
    }
}

