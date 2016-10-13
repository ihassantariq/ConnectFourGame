package com.convension.connectfour.activity;

import  com.convension.connectfour.Connect4App;
import  com.convension.connectfour.R;
import com.convension.connectfour.board.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class SettingsActivity extends ABaseActivity implements RadioGroup.OnCheckedChangeListener {
	
     private RadioGroup soundGroup;
     private RadioButton sound0,sound1;
     private Button doneButton;
    private RadioGroup diffGroup;
    private RadioGroup turnGroup;
    private RadioButton diff0, diff1, diff2;
    private RadioGroup playGroup;
    private RadioButton play0,play1;
    private RadioButton turn0,turn1;
    private RadioGroup powerGroup;
    private RadioButton power0,power1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        init();
        load();
    }
    
    private void setSound(int i){
    	if(i==Players.SOUND_ON){sound0.setChecked(true);}
    	else if(i==Players.SOUND_OFF){sound1.setChecked(true);}
    }
    
    private int getSound(){
    	if(soundGroup.getCheckedRadioButtonId()==R.id.radio_sound0){
    		return Players.SOUND_ON;
    	}
    	else {
    		return Players.SOUND_OFF;
    	}
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
    	super.onRestoreInstanceState(savedInstanceState);
    }
    public void onRestart(){
    	super.onRestart();
    	//load();
    }
    public void onStart(){
    	super.onStart();
    	//load();
    }
    public void onResume(){
    	super.onResume();
    	//load();
    }
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    }
    public void onPause(){
    	super.onPause();
    }
    public void onStop(){
    	super.onStop();
    }
    public void onDestroy(){
    	super.onDestroy();
    }
    private void setDiff(int i){
        if(i==Players.DIFF_EASY){diff0.setChecked(true);}
        else if(i==Players.DIFF_MED){diff1.setChecked(true);}
        else if(i==Players.DIFF_HARD){diff2.setChecked(true);}
    }
    private void setPower(int i){
        if(i==Players.POWER_ON){power0.setChecked(true);}
        else if(i==Players.POWER_OFF){power1.setChecked(true);}
    }
    private void setPlayers(int i){
        if(i==Players.ONE_PLAYER){play0.setChecked(true);}
        else if(i==Players.TWO_PLAYERS){play1.setChecked(true);}
    }
    private void setTurn(int i){
        if(i==Players.GO_FIRST){turn0.setChecked(true);}
        else if(i==Players.GO_SECOND){turn1.setChecked(true);}
    }
    private int getTurn(){
        if(turnGroup.getCheckedRadioButtonId()==R.id.radio_turn0){
            return 0;
        }
        else {
            return 1;
        }
    }
    private int getPower(){
        if(powerGroup.getCheckedRadioButtonId()==R.id.radio_power0){
            return Players.POWER_ON;
        }
        else {
            return Players.POWER_OFF;
        }
    }
    private int getDiff(){
        if(diffGroup.getCheckedRadioButtonId()==R.id.radio_diff0){
            return 0;
        }
        else if(diffGroup.getCheckedRadioButtonId()==R.id.radio_diff1){
            return 1;
        }
        else {
            return 2;
        }
    }
    private int getPlay(){
        if(playGroup.getCheckedRadioButtonId()==R.id.radio_play0){
            return 0;
        }
        else {
            return 1;
        }

    }
    private void store(){
        SharedPreferences settings = getSharedPreferences(Connect4App.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int s = getSound();
        int d = getDiff();
        int p = getPlay();
        int t = getTurn();
        int pw = getPower();
        editor.putInt(Connect4App.PREFS_DIFF,d);
        editor.putInt(Connect4App.PREFS_PLAY,p);
        editor.putInt(Connect4App.PREFS_TURN,t);
        editor.putInt(Connect4App.PREFS_POWER,pw);
        editor.putInt (Connect4App.PREFS_THEME,Players.THEME_DEFAULT);
        editor.putInt(Connect4App.PREFS_SOUND,s);
        editor.commit();
    }
    private void load(){
        SharedPreferences settings = getSharedPreferences(Connect4App.PREFS_NAME, 0);
        int d = settings.getInt(Connect4App.PREFS_DIFF, Players.DIFF_EASY);
        int p = settings.getInt(Connect4App.PREFS_PLAY, Players.ONE_PLAYER);
        int t = settings.getInt(Connect4App.PREFS_TURN, Players.GO_FIRST);
        int pw = settings.getInt(Connect4App.PREFS_POWER, Players.POWER_OFF);
        int s = settings.getInt(Connect4App.PREFS_SOUND, Players.SOUND_ON);
        setSound(s);
        showTurn();
        setDiff(d);
        setPlayers(p);
        setTurn(t);
        setPower(pw);
        this.showTurn();
    }
    private void init(){
        soundGroup = (RadioGroup) findViewById(R.id.radio_sound);
        sound0 = (RadioButton) findViewById(R.id.radio_sound0);
        sound1 = (RadioButton) findViewById(R.id.radio_sound1);
        doneButton = (Button) findViewById(R.id.done_options);
        doneButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                store();
                finish();
            }
        });
        diffGroup = (RadioGroup) findViewById(R.id.radio_diff);
        playGroup = (RadioGroup) findViewById(R.id.radio_play);
        turnGroup = (RadioGroup) findViewById(R.id.radio_turn);
        diff0 = (RadioButton) findViewById(R.id.radio_diff0);
        diff1 = (RadioButton) findViewById(R.id.radio_diff1);
        diff2 = (RadioButton) findViewById(R.id.radio_diff2);
        play0 = (RadioButton) findViewById(R.id.radio_play0);
        play1 = (RadioButton) findViewById(R.id.radio_play1);
        turn0 = (RadioButton) findViewById(R.id.radio_turn0);
        turn1 = (RadioButton) findViewById(R.id.radio_turn1);
        playGroup.setOnCheckedChangeListener(this);
        powerGroup = (RadioGroup) findViewById(R.id.radio_power);
        power0 = (RadioButton) findViewById(R.id.radio_power0);
        power1 = (RadioButton) findViewById(R.id.radio_power1);
    }


    private void showTurn(){
        boolean show = (getPlay()==Players.ONE_PLAYER);
        turnGroup.setEnabled(show);
        turn0.setEnabled(show);
        turn1.setEnabled(show);
        diff0.setEnabled(show);
        diff1.setEnabled(show);
        diff2.setEnabled(show);
        diffGroup.setEnabled(show);

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        showTurn();
    }
    
   
}



