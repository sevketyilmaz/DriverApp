package com.taximobile.zdriverapp.fragments;



import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.taximobile.zdriverapp.R;
import com.taximobile.zdriverapp.background.PositionManager;
import com.taximobile.zdriverapp.background.PositionReceiver;

public class PositionFragment extends Fragment {
	private static final String TAG = "PositionFragment";
	
	private PositionManager _positionManager;
	
	private BroadcastReceiver _positionReceiver = new PositionReceiver(){
		@Override
		protected void onLocationReceived(Context context ,Location loc) {
			if(isVisible() && loc != null){
				String message = String.format("LAT/LNG: %f / %f", loc.getLatitude(), loc.getLongitude());
				txtView.setText(message);
			}
		};
		
		@Override
		protected void onProviderEnabledChanged(boolean enabled) {
			String toastText = enabled ? "GPS enabled" : "GPS disabled";
			Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
		};
	};
	
	private TextView txtView;
	private Button startButton, stopButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		_positionManager = PositionManager.Get(getActivity(), PositionManager.APP_TYPE_DRIVER);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_position, container, false);
		
		txtView = (TextView) view.findViewById(R.id.txtView);
		startButton = (Button) view.findViewById(R.id.startButton);
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_positionManager.startLocationUpdates();
				updateUI();
			}
		});
		
		stopButton = (Button) view.findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				_positionManager.stopLocationUpdates();
				updateUI();
			}
		});
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(_positionReceiver,
				new IntentFilter(PositionManager.ACTION_LOCATION));
	}
	
	@Override
	public void onStop() {
		getActivity().unregisterReceiver(_positionReceiver);
		_positionManager.stopLocationUpdates();
		super.onStop();
	}
	
	private void updateUI(){
		boolean started = _positionManager.isTrackingPosition();
		
		startButton.setEnabled(!started);
		stopButton.setEnabled(started);
		
		
	}
	
}
