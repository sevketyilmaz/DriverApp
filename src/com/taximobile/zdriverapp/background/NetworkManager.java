package com.taximobile.zdriverapp.background;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Log;

import com.taximobile.zdriverapp.MainActivity;
import com.taximobile.zdriverapp.model.Driver;
import com.taximobile.zdriverapp.model.LoginModel;
import com.taximobile.zdriverapp.model.ModelManager;

public class NetworkManager {
	private static final String TAG = "NetworkManager";
	
	public static void LogOnDriver(LoginModel loginModel) throws ClientProtocolException, IOException{
		try {
			
			//Create a client, post request and parameters array list
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(MainActivity.TM_LOGON_DRIVER);
			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			
			//set request information 
			parameters.add(new BasicNameValuePair("UserName", loginModel.getUserName()));
			parameters.add(new BasicNameValuePair("Password", loginModel.getPassword()));
			parameters.add(new BasicNameValuePair("NumberPlate", loginModel.getNumberPlate()));
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
			
			//execute the request
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			//if response is ok
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				//get response content
				HttpEntity httpEntity = httpResponse.getEntity();
				
				//convert the entity {resulting page} to string and parse it
				String content = EntityUtils.toString(httpEntity, "UTF-8");
				
				Log.d(TAG, "content : " + content);
				if (content.equals("null")) {
					ModelManager.Get().setDriver(null);
					return;
				}
				parseTheContent(content);	
				
			}else{
				ModelManager.Get().setDriver(null);
				return;
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Error occured while dowbloading", e);
			ModelManager.Get().setDriver(null);
			return;
		}
	}
	
	private static void parseTheContent(String content){
		try {
			JSONObject driverJO = new JSONObject(content);
			
			int driverId = Integer.parseInt(driverJO.getString("DriverId"));
			String driverName = driverJO.getString("DriverName");
			String email = driverJO.getString("Email");
			String password = driverJO.getString("Password");
			String gsm = driverJO.getString("GSM");
			String driverLicence = driverJO.getString("DriverLicence");
			
			int vehicleId = Integer.parseInt(driverJO.getString("VehicleId"));
			String numberPlate = driverJO.getString("NumberPlate");
			
			Driver d = new Driver(driverId, driverName, email, password, gsm, driverLicence, vehicleId, numberPlate);
			
			ModelManager.Get().setDriver(d);
			return;
			
		} catch (Exception e) {
			Log.e(TAG, "Error occured while dowbloading", e);
			ModelManager.Get().setDriver(null);
			return;
		}
	}
}
