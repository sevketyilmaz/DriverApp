package com.taximobile.zdriverapp.background;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.taximobile.zdriverapp.model.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

public class PositionPushAsyncTask extends AsyncTask<Void, String, Void>{
	private static final String TAG = "PositionPushAsyncTask";
	private Context _context; //Activity context
	private ProgressDialog _progressDialog;
	
	private Driver _driver;
	private Location _loc;
	
	//constructor
	public PositionPushAsyncTask(Context context, Location loc) {
		super();
		_context = context;
		_loc = loc;
	}
	
	@Override
	protected void onPreExecute() {
		_progressDialog = ProgressDialog.show(_context, "Please Wait" , "Process in progress", true, true);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		_driver = ModelManager.Get().getDriver();
		
		publishProgress("Updating the OnlineVehicle location");
		
		try{
			NetworkManager.PositionPush(_loc);
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		_progressDialog.cancel();
		//TODO call the callback??
	}
	
	@Override
	protected void onProgressUpdate(String... progress) {
		_progressDialog.setMessage(progress[0]);
	}
	

}
