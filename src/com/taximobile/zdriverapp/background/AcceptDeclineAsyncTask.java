package com.taximobile.zdriverapp.background;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.os.AsyncTask;

public class AcceptDeclineAsyncTask extends AsyncTask<Integer, Void, Void>{
	private static final String TAG = "AcceptDeclineAsyncTask";
	private Context _context; //activity context
	
	//constructor
	public AcceptDeclineAsyncTask(Context context){
		_context = context;
	}
	
	@Override
	protected Void doInBackground(Integer... params) {		
		int acceptDecline = params[0];
		
		try{
			NetworkManager.AcceptDecline(acceptDecline);
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
		
	}

}
