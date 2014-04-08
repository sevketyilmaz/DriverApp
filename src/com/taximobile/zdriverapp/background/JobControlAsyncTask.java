package com.taximobile.zdriverapp.background;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class JobControlAsyncTask extends AsyncTask<Void, String, Integer>{
	private static final String TAG = "JobControlAsyncTask";
	private static int JOB_EXIST = 1;
	private static int JOB_NONE = 2;
	
	private Context _context;
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
	protected Integer doInBackground(Void... params) {
		publishProgress("Checking the job status");
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		_progressDialog.cancel();
		//TODO call the callback
		
	}
	
	@Override
	protected void onProgressUpdate(String... progress) {
		_progressDialog.setMessage(progress[0]);
	}	
	

}
