package com.convension.connectfour.activity;

import com.convension.connectfour.Connect4App;
import com.convension.connectfour.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.convension.connectfour.board.Players;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;


public class MainMenuActivity extends ABaseActivity implements View.OnClickListener {

	private Button setButton;
	private Button playButton;
	private Button helpButton;
	private Button mMultiplayer;
	public AdView mAdView;
	
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
		mAdView = (AdView ) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().
				addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build ();
		mAdView.loadAd(adRequest);
        init();
    }

    private void init(){
    	playButton = (Button)(this.findViewById(R.id.play_button));
    	playButton.setOnClickListener(this);
		mMultiplayer=(Button)(this.findViewById(R.id.multiplayer_button));
		mMultiplayer.setOnClickListener(this);
    	setButton = (Button)(this.findViewById(R.id.open_settings));
    	setButton.setOnClickListener(this);
    	helpButton = (Button)(this.findViewById(R.id.open_help));
    	helpButton.setOnClickListener(this);
    }

   
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
    	super.onRestoreInstanceState(savedInstanceState);
    }
    public void onRestart(){
    	super.onRestart();
    }
    public void onStart(){
    	super.onStart();
    }
    public void onResume(){

		super.onResume();
		mAdView.resume();
    }
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    }
    public void onPause()
	{
		mAdView.pause();
    	super.onPause();
    }
    public void onStop(){
    	super.onStop();
    }
    public void onDestroy(){
    	super.onDestroy();
    }
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.open_settings){
			Intent intent = new Intent(this, SettingsActivity.class);
	        startActivity(intent);
		}
		else if(v.getId()==R.id.multiplayer_button){
			Intent intent = new Intent(this, Connect4MultiplayerActivity.class);
			startActivity(intent);
		}
		else if(v.getId()==R.id.play_button){
			Intent intent = new Intent(this, Connect4Activity.class);
	        this.startActivity(intent);
		}
		else if(v.getId()==R.id.open_help){
			Intent intent = new Intent(this, HelpActivity.class);
	        this.startActivity(intent);
		}
		
	}
   
}