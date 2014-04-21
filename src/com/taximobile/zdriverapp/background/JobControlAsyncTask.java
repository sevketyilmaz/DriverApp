package com.taximobile.zdriverapp.background;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.taximobile.zdriverapp.model.ModelManager;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class JobControlAsyncTask extends AsyncTask<Void, String, Void>{
	private static final String TAG = "JobControlAsyncTask";
	
	private Context _context; //activity context
	private ProgressDialog _progressDialog;
	
	public interface IJobControlReadyListener{
		public void JobControlReady(int jobStatus);
	}
	private IJobControlReadyListener _listener;
	
	//constructor
	public JobControlAsyncTask(Context context, Fragment fragment) {
		super();
		_context = context;
		_listener = (IJobControlReadyListener)fragment;
	}
	
	@Override
	protected void onPreExecute() {
		_progressDialog = ProgressDialog.show(_context, "Please Wait" , "Process in progress", true, true);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		publishProgress("Checking the job status");
		
		try{
			NetworkManager.JobControl(); //onlineVehicle control
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

		//TODO call the callback in ScreenFragment
		int status = (ModelManager.Get().getJob() == null) ? 2 : 1; 
		_listener.JobControlReady(status);//result = job status : (1-JOB_EXIST ; 2-JOB_NONE)
	}
	
	@Override
	protected void onProgressUpdate(String... progress) {
		_progressDialog.setMessage(progress[0]);
	}	
	

}
