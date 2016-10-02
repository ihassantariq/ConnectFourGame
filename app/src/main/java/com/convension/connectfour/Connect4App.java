package com.convension.connectfour;

import android.app.Application;

import com.convension.connectfour.utils.ConnectivityReceiver;
import com.google.android.gms.ads.MobileAds;

public class Connect4App extends Application{
	
	public static final String PREFS_NAME 		= 	"MyPrefsFile";
	public static final String PREFS_SOUND 		= 	"sound";
	public static final String PREFS_PLAY 		= 	"player";
	public static final String PREFS_DIFF 		= 	"difficulty";
	public static final String PREFS_TURN 		= 	"turn";
	public static final String PREFS_POWER 		= 	"power";
	public static final String PREFS_THEME		=	"theme";
	
	public static boolean DEBUG = false;
	
	public Connect4App(){
		super();
	}
	private static Connect4App mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		MobileAds.initialize(getApplicationContext(), getString (R.string.add_app_id));

		mInstance = this;
	}

	public static synchronized Connect4App getInstance() {
		return mInstance;
	}

	public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
		ConnectivityReceiver.connectivityReceiverListener = listener;

	}
	
}