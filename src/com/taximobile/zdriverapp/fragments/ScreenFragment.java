package com.taximobile.zdriverapp.fragments;

import android.R;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.taximobile.zdriverapp.background.PositionManager;
import com.taximobile.zdriverapp.background.PositionReceiver;
import com.taximobile.zdriverapp.model.Driver;
import com.taximobile.zdriverapp.model.ModelManager;

public class ScreenFragment extends Fragment{
	private static final String TAG = "ScreenFragment";
	
	private PositionManager _positionManager;
	private Driver _driver; //ModelManager.Get().getDriver()
	
	private Switch dutySwitch;
	private TextView txtView;
	
	private BroadcastReceiver _positionReceiver = new PositionReceiver(){
		@Override
		protected void onLocationReceived(Context context, Location loc) {
			if(isVisible() && loc != null){
				//async task i calistir
				positionPush(loc);
			}
		};
		
		@Override
		protected void onProviderEnabledChanged(boolean enabled) {
			String toastText = enabled ? "GPS enabled" : "GPS disabled";
			Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
		};
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_driver = ModelManager.Get().getDriver();
		_positionManager = PositionManager.Get(getActivity(), PositionManager.APP_TYPE_DRIVER);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_screen, container, false);
		
		
	}
	
	
	
}
