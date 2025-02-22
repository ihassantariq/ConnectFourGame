package com.convension.connectfour.views;

import  com.convension.connectfour.R;
import  com.convension.connectfour.inter.*;
import com.convension.connectfour.utils.Util;
import com.convension.connectfour.board.*;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopView extends RelativeLayout implements IOnTurnChangeListener{

	private Resources res;

	public TextView getT1() {
		return t1;
	}

	private TextView t1;

	public TextView getT2() {
		return t2;
	}

	public Resources getRes() {
		return res;
	}

	private TextView t2;
	private ImageView mLeftImageView,mRightImageView;
	public int turn = Players.PLAYER1;
	public Context mContext;
	
	public TopView(Context c, AttributeSet a){
		super(c,a);
		mContext=c;
		init();
	}
	public TopView(Context c){
		super(c);
		mContext=c;
		init();
	}
	public void setTurn(int i){
		turn = i;
		this.postInvalidate();
	}
	private void inflate(){
		LayoutInflater linf = (LayoutInflater)(getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		linf.inflate(R.layout.top, this);

	}
	private int dpToPix(float f){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, f, res.getDisplayMetrics());
	}
	private void init(){
		inflate();
		res = getResources();
		this.setWillNotDraw(false);
		t1 = (TextView)this.findViewById(R.id.your_text);
		t2 = (TextView)this.findViewById(R.id.comp_text);
		mLeftImageView=(ImageView)this.findViewById (R.id.left_icon);
		mRightImageView=(ImageView)this.findViewById (R.id.right_icon);
		adjustTheme();
	}
	private void adjustTheme() {
		Util.getInstance ().setIcons (mLeftImageView,mRightImageView,mContext);
	}
	public void setNumPlayers(int p){
		if(p==Players.ONE_PLAYER){
			t1.setText("You");
			t2.setText("Computer");
		}
		else{
			t1.setText(Players.FIRST_PLAYER);
			t2.setText(Players.SECOND_PLAYER);
		}
		//Server Player
		if(Players.IS_MULTIPLAYER_SERVER) {
			//if(Players.IS_SERVER) {
				if(Players.FIRST_PLAYER.length ()>15) {
					t1.setText (Players.FIRST_PLAYER.substring (0,14)+"..");
				}else{
					t1.setText (Players.FIRST_PLAYER);
				}
				if(Players.SECOND_PLAYER.length ()>15) {
					t2.setText (Players.SECOND_PLAYER.substring (0,14)+"..");
				}else{
					t2.setText (Players.SECOND_PLAYER);
				}
//			}else{
//				if(Players.FIRST_PLAYER.length ()>15) {
//					t2.setText (Players.FIRST_PLAYER.substring (0,14)+"..");
//				}else{
//					t2.setText (Players.FIRST_PLAYER);
//				}
//				if(Players.SECOND_PLAYER.length ()>15) {
//					t1.setText (Players.SECOND_PLAYER.substring (0,14)+"..");
//				}else{
//					t1.setText (Players.SECOND_PLAYER);
//				}
//			}
		}
	}
	@Override
	protected void onDraw(Canvas c){
		float x0,x1,y0,y1;
		int padSides = dpToPix(4);
		int round = dpToPix(1F);
		int padBottom = dpToPix(3);
		
		int e1Pix = dpToPix(1F);	
		int e2Pix = dpToPix(2F);
		int e3Pix = dpToPix(3.5F);
		
		try{
			
			Paint linePaint = new Paint();
			linePaint.setStyle(Style.FILL);
			if(turn==Players.PLAYER1){
				x0 = padSides;
				x1 = t1.getRight()+padSides;
				y0 = t1.getBottom()+padBottom;
				y1 = y0+2*round;
				t1.setTextColor(Color.WHITE);
				t2.setTextColor(Color.GRAY);
			}
			else{
				x0 = t2.getLeft()-padSides;
				x1 = this.getWidth()-1;
				y0 = t2.getBottom()+padBottom;
				y1 = y0+2*round;
				t1.setTextColor(Color.GRAY);
				t2.setTextColor(Color.WHITE);
			}
			
			
			RectF r1 = new RectF(x0,y0,x1,y1);
			RectF r2 = new RectF(x0-e1Pix,y0-e1Pix,x1+e1Pix,y1+e1Pix);
			RectF r3 = new RectF(x0-e2Pix,y0-e2Pix,x1+e2Pix,y1+e2Pix);
			RectF r4 = new RectF(x0-e3Pix,y0-e3Pix,x1+e3Pix,y1+e3Pix);
			
			linePaint.setColor(res.getColor(R.color.wood_dark));
			c.drawRoundRect(r4,  round, round, linePaint);
			linePaint.setColor(res.getColor(R.color.wood_meddark));
			c.drawRoundRect(r3,  round, round, linePaint);
			linePaint.setColor(res.getColor(R.color.wood_medlight));
			c.drawRoundRect(r2,  round, round, linePaint);
			linePaint.setColor(res.getColor(R.color.wood_light));
			c.drawRoundRect(r1,  round, round, linePaint);
			
		}
		catch(Exception e){
			// not there yet
		}
		super.onDraw(c);
	}
	@Override
	public boolean onChange(View v, int t) {
		turn = t;
		this.postInvalidate();
		return false;
	}
}



