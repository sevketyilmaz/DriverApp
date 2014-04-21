package com.taximobile.zdriverapp.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.taximobile.zdriverapp.R;
import com.taximobile.zdriverapp.background.JobControlAsyncTask;
import com.taximobile.zdriverapp.background.PositionManager;
import com.taximobile.zdriverapp.background.PositionPushAsyncTask;
import com.taximobile.zdriverapp.background.PositionReceiver;
import com.taximobile.zdriverapp.model.*;

public class ScreenFragment extends Fragment implements JobControlAsyncTask.IJobControlReadyListener{
	private static final String TAG = "ScreenFragment";
	
	private PositionManager _positionManager;
	private Driver _driver; //ModelManager.Get().getDriver()
	
	private Switch dutySwitch;
	private TextView txtView;
	private TextView txtLoc;
	
	private BroadcastReceiver _positionReceiver = new PositionReceiver(){
		@Override
		protected void onLocationReceived(Context context, Location loc) {
			if(isVisible() && loc != null){				
				//async tasklari calistir
				jobControl();
				
				
				positionPush(loc); //push the position and update onlineVehicle model location
				

				
			}
		};
		
		@Override
		protected void onProviderEnabledChanged(boolean enabled) {
			String toastText = enabled ? "GPS enabled" : "GPS disabled";
			Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
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
		
		dutySwitch = (Switch) view.findViewById(R.id.dutySwitch);
		txtView = (TextView) view.findViewById(R.id.txtView);
		txtLoc = (TextView) view.findViewById(R.id.txtView2);
		
		//set switch to on
		dutySwitch.setChecked(true);
		
		//check current state of switch
		if(dutySwitch.isChecked()){
			OnDutySwitch();
		}else{
			OffDutySwitch();
		}
		
		//attach listener for switch changes
		dutySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					OnDutySwitch();
				}else{
					OffDutySwitch();
				}				
			}
		});
		
		return view;
	}
	
	@Override
	public void JobControlReady(int jobStatus) {
		// TODO Auto-generated method stub
		if(jobStatus == 1){ // there is a new job
			Toast.makeText(getActivity(), "There is a new job !! create accept/deline", Toast.LENGTH_LONG).show();
			//TODO create a accept decline and after that synchronize the OnlineVehicleStatus
			
		}else if(jobStatus == 2){// there is no job
			
		}
		
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
	
	private void OnDutySwitch(){
		txtView.setText("We are on Duty");
		_positionManager.startLocationUpdates();
	}
	
	private void OffDutySwitch(){
		txtView.setText("we are in Break");
		_positionManager.stopLocationUpdates();
	}
	
	private void positionPush(Location loc) {		
		//Driver driver = new Driver(2, 2);
		//PositionPushAsyncTask task = new PositionPushAsyncTask(getActivity(), this, loc);
		//task.execute(_driver);
		String location = String.format("Latitude= %.3f : Longitude= %.3f", loc.getLatitude(), loc.getLongitude());
		txtLoc.setText(location);
		
		PositionPushAsyncTask task = new PositionPushAsyncTask(getActivity(), loc);
		task.execute();
	}
	
	private void jobControl(){
		//it has a callback method JobControlReady()
		if(ModelManager.Get().getOnlineVehicle().getStatusId() == OnlineVehicle.STATUS_AVAILABLE){
		JobControlAsyncTask task = new JobControlAsyncTask(getActivity(), this);
		task.execute();
		}
	}



}
