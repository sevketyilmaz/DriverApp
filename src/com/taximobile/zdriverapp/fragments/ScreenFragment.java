package com.taximobile.zdriverapp.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.taximobile.zdriverapp.R;
import com.taximobile.zdriverapp.background.AcceptDeclineAsyncTask;
import com.taximobile.zdriverapp.background.JobControlAsyncTask;
import com.taximobile.zdriverapp.background.PositionManager;
import com.taximobile.zdriverapp.background.PositionPushAsyncTask;
import com.taximobile.zdriverapp.background.PositionReceiver;
import com.taximobile.zdriverapp.model.Driver;
import com.taximobile.zdriverapp.model.Job;
import com.taximobile.zdriverapp.model.ModelManager;
import com.taximobile.zdriverapp.model.OnlineVehicle;

public class ScreenFragment extends Fragment implements JobControlAsyncTask.IJobControlReadyListener{
	private static final String TAG = "ScreenFragment";
	
	private PositionManager _positionManager;
	private Driver _driver; //ModelManager.Get().getDriver()
	
	View view;
	Dialog d;
	Button accept, decline;
	TextView txtCountDown;
	MediaPlayer mp;
	SoundPool sp;
	final MyCounter timer = new MyCounter(6000, 1000);
	
	private Switch dutySwitch;
	private TextView txtView;
	private TextView txtLoc;
	
	private BroadcastReceiver _positionReceiver = new PositionReceiver(){
		@Override
		protected void onLocationReceived(Context context, Location loc) {
			if(isVisible() && loc != null){
				//set the online vehicle location
				if(ModelManager.Get().getOnlineVehicle() == null)
					ModelManager.Get().setOnlineVehicle(new OnlineVehicle());
				ModelManager.Get().getOnlineVehicle().setLat(loc.getLatitude());
				ModelManager.Get().getOnlineVehicle().setLng(loc.getLongitude());
				
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
		view = inflater.inflate(R.layout.fragment_screen, container, false);
		
		dutySwitch = (Switch) view.findViewById(R.id.dutySwitch);
		txtView = (TextView) view.findViewById(R.id.txtView);
		txtLoc = (TextView) view.findViewById(R.id.txtLoc);
		
		
		
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
		if(jobStatus == 1){ // there is a new job
			//TODO create a accept decline and after that synchronize the OnlineVehicleStatus
			/*
			 * Dialog for accept / decline job 
			 * */
			createDialog();

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

	public class MyCounter extends CountDownTimer{
		public MyCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			declineTheJob();
			d.dismiss();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			txtCountDown.playSoundEffect(2);
			txtCountDown.setText("Timer: " + (millisUntilFinished/1000)+ " seconds remaining");	
		}		
	}
	
	private void createDialog() {
		Log.d(TAG, "JobControl_DialogCreate");
		if(d!=null && d.isShowing()) d.dismiss();
		d = new Dialog(getActivity());
		View vLoad = LayoutInflater.from(getActivity()).inflate(
				R.layout.job_accept_decline, null);
		d.setContentView(vLoad);
		txtCountDown = (TextView) d.findViewById(R.id.txtCountDown);
		accept = (Button) d.findViewById(R.id.acceptButton);
		decline = (Button) d.findViewById(R.id.declineButton);
		d.setTitle("Accept / Decline job");
		d.setCancelable(false);			
		txtCountDown.setTextColor(Color.RED);
		
		/*sound start*/
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		int iTmp = sp.load(getActivity(), R.raw.job_alert, 1);
		sp.play(iTmp, 1, 1, 0, 0, 1);
		mp = MediaPlayer.create(getActivity(), R.raw.job_alert);
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer arg0) {
				mp.release();
			}
		});
		mp.start();
		/*sound end*/
		
		timer.start();
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.cancel();
				acceptTheJob();
				d.dismiss();
			}
		});
		decline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.cancel();
				declineTheJob();
				d.dismiss();
			}
		});
		d.show();		
	}
	
	private void acceptTheJob(){
		//job and customer objects are exist but not complete
		//for complete check the NetworkManager ParseTheJobContent
		ModelManager.Get().getOnlineVehicle().setStatusId(2); //set the onlineVehicle object statusId = 2 (Busy)
		
		//TODO set the OnlineVehicle statusId to busy=2
		AcceptDeclineAsyncTask acceptTask = new AcceptDeclineAsyncTask(getActivity());
		acceptTask.execute(Job.ACCEPT_JOB);//ACCEPT_JOB = 1

	}
	
	private void declineTheJob(){
		//TODO
		//set the job and customer objects to null and onlineVehicle's jobId to 0
		if(ModelManager.Get().getCustomer() != null)
			ModelManager.Get().setCustomer(null);
		if(ModelManager.Get().getJob() != null)
			ModelManager.Get().setJob(null);
		ModelManager.Get().getOnlineVehicle().setStatusId(1);
		ModelManager.Get().getOnlineVehicle().setJobId(0);
		
		//set the OnlineVehicle jobId to null		
		AcceptDeclineAsyncTask declineTask = new AcceptDeclineAsyncTask(getActivity());
		declineTask.execute(Job.DECLINE_JOB);//DECLINE_JOB = 2
	}

}
