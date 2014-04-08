package com.taximobile.zdriverapp.background;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.taximobile.zdriverapp.model.LoginModel;
import com.taximobile.zdriverapp.model.ModelManager;

public class LogOnAsyncTask extends AsyncTask<Void, String, Void>{
	private static final String TAG = "LogOnAsyncTask";
	private Context _context;//Activity Context
	private ProgressDialog _progressDialog;
	
	public interface ILogOnReadyListener{
		public void LogOnReady();
	}
	private ILogOnReadyListener _listener;
	
	//constructor
	public LogOnAsyncTask(Context context, Fragment fragment) {
		super();
		_context = context;
		_listener = (ILogOnReadyListener) fragment;
	}
	
	@Override
	protected void onPreExecute() {
		_progressDialog = ProgressDialog.show(_context, "Please Wait" , "Process in progress", true, true);
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		LoginModel loginModel = ModelManager.Get().getLoginModel();
		publishProgress("Loging in to the system");
		
		try{
			NetworkManager.LogOnDriver(loginModel);
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
		_listener.LogOnReady();
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		_progressDialog.setMessage(progress[0]);
	}
	
}
