package com.taximobile.zdriverapp.background;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.location.Location;
import android.util.Log;

import com.taximobile.zdriverapp.MainActivity;
import com.taximobile.zdriverapp.model.Customer;
import com.taximobile.zdriverapp.model.Driver;
import com.taximobile.zdriverapp.model.Job;
import com.taximobile.zdriverapp.model.LoginModel;
import com.taximobile.zdriverapp.model.ModelManager;
import com.taximobile.zdriverapp.model.OnlineVehicle;

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
			if(ModelManager.Get().getOnlineVehicle() == null){
				ModelManager.Get().setOnlineVehicle(new OnlineVehicle());
				ModelManager.Get().getOnlineVehicle().setDriverId(driverId);
				ModelManager.Get().getOnlineVehicle().setVehicleId(vehicleId);
			}else{
				ModelManager.Get().getOnlineVehicle().setDriverId(driverId);
				ModelManager.Get().getOnlineVehicle().setVehicleId(vehicleId);
			}
			return;
			
		} catch (Exception e) {
			Log.e(TAG, "Error occured while dowbloading", e);
			ModelManager.Get().setDriver(null);
			return;
		}
	}

	public static void PositionPush(Location loc) throws ClientProtocolException, IOException{
		try {
			Driver driver = ModelManager.Get().getDriver();
			
			if(ModelManager.Get().getOnlineVehicle() == null)
				ModelManager.Get().setOnlineVehicle(new OnlineVehicle());//sets status id default to 1;
			
			
			ModelManager.Get().getOnlineVehicle().setLastUpdate(System.currentTimeMillis());
			ModelManager.Get().getOnlineVehicle().setLat(loc.getLatitude());
			ModelManager.Get().getOnlineVehicle().setLng(loc.getLongitude());
			ModelManager.Get().getOnlineVehicle().setDriverId(driver.getDriverId());
			ModelManager.Get().getOnlineVehicle().setVehicleId(driver.getVehicleId());
			if(ModelManager.Get().getJob() != null)
				ModelManager.Get().getOnlineVehicle().setJobId(ModelManager.Get().getJob().getJobId());
			//for testing!!!!!!!!!!
			//ModelManager.Get().getJob().setJobId(1);
			
			//Create a client, a post request and parameters array list
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPut httpPut = new HttpPut(MainActivity.TM_POSITION_PUSH);
			
			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			
			//{"VehicleId":2,"DriverId":2,"StatusId":1,"Lat":26.2,"Lng":36.2}
			//set request information
			parameters.add(new BasicNameValuePair("VehicleId", String.valueOf(driver.getVehicleId())));
			parameters.add(new BasicNameValuePair("DriverId", String.valueOf(driver.getDriverId())));
			parameters.add(new BasicNameValuePair("Lat", String.valueOf(loc.getLatitude())));
			parameters.add(new BasicNameValuePair("Lng", String.valueOf(loc.getLongitude())));
			parameters.add(new BasicNameValuePair("StatusId",
					String.valueOf(ModelManager.Get().getOnlineVehicle().getStatusId()))); //StatusId =1 Default =available
			parameters.add(new BasicNameValuePair("LastUpdate",
					ModelManager.Get().getOnlineVehicle().getLastUpdate().toString()));
			
			if (ModelManager.Get().getJob() != null) {
				parameters.add(new BasicNameValuePair("JobId", String
						.valueOf(ModelManager.Get().getJob().getJobId())));
				Log.d(TAG, "jobId: " + ModelManager.Get().getJob().getJobId());
			}
			httpPut.setEntity(new UrlEncodedFormEntity(parameters));
						
			//execute the request
			HttpResponse httpResponse = httpClient.execute(httpPut);
			
			//if response status code, No Content
			//if(httpResponse.getStatusLine().getStatusCode() == 204){}
		
			//return anyway
			return;			
		} catch (Exception e) {
			Log.e(TAG, "Error occured while pushing the location to server " + e);
			return;
		}
		
	}

	public static void JobControl() throws ClientProtocolException, IOException{ //normally gets online vehicle
		try {
			OnlineVehicle ov = ModelManager.Get().getOnlineVehicle();
			
			//Create client, Post request and parameter list
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(MainActivity.TM_JOB_CONTROL);
			
			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			
			parameters.add(new BasicNameValuePair("VehicleId", String.valueOf(ov.getVehicleId())));
			parameters.add(new BasicNameValuePair("DriverId", String.valueOf(ov.getDriverId())));
			parameters.add(new BasicNameValuePair("StatusId", String.valueOf(ov.getStatusId())));
			
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			//if response is ok
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				//get response content
				HttpEntity httpEntity = httpResponse.getEntity();
				
				//convert the entity {resulting page} to string and parse it
				String content = EntityUtils.toString(httpEntity, "UTF-8");
				
				Log.d(TAG, "content Job Control : " + content);
				if (content.equals("null")) {
					ModelManager.Get().setJob(null);
					return;
				}
				parseTheJobControlContent(content);	//and update job,customer object
				
			}else{
				ModelManager.Get().setJob(null);
				return;
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Error occured while JobControlling " + e);
			return;
		}
		
	}

	private static void parseTheJobControlContent(String content){
		try {
			JSONObject JobControlJO = new JSONObject(content);
			
			int jobId = Integer.parseInt(JobControlJO.getString("JobId"));
			int driverId = Integer.parseInt(JobControlJO.getString("DriverId"));
			int vehicleId = Integer.parseInt(JobControlJO.getString("VehicleId"));
			int customerId = Integer.parseInt(JobControlJO.getString("CustomerId"));
			String requestDate = JobControlJO.getString("RequestDate");
			
			double customerLat = Double.parseDouble(JobControlJO.getString("PickupLat"));
			double customerLng = Double.parseDouble(JobControlJO.getString("PickupLng"));

			Job j = new Job(jobId, driverId, vehicleId, customerId, requestDate);
			ModelManager.Get().setJob(j);
			
			if (ModelManager.Get().getCustomer() == null){
				Customer c = new Customer(customerId, customerLat, customerLng);
				ModelManager.Get().setCustomer(c);
			}else{
				ModelManager.Get().getCustomer().setCustomerId(customerId);
				ModelManager.Get().getCustomer().setLat(customerLat);
				ModelManager.Get().getCustomer().setLng(customerLng);
			}			
			
			return;
			
		} catch (Exception e) {
			Log.e(TAG, "Error occured while dowbloading", e);
			ModelManager.Get().setJob(null);
			return;
		}
	}

	public static void AcceptDecline(int acceptDecline) throws ClientProtocolException, IOException{ //normally gets online vehicle
		try {
			OnlineVehicle ov = ModelManager.Get().getOnlineVehicle();
			Driver d = ModelManager.Get().getDriver();
			
			//Create a client, post request and parameters array list
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(MainActivity.TM_JOB_ACCEPT_DECLINE);
			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			
			parameters.add(new BasicNameValuePair("VehicleId", String.valueOf(d.getVehicleId())));
			parameters.add(new BasicNameValuePair("DriverId", String.valueOf(d.getDriverId())));
			parameters.add(new BasicNameValuePair("Lat", String.valueOf(ov.getLat())));
			parameters.add(new BasicNameValuePair("Lng", String.valueOf(ov.getLng())));
			parameters.add(new BasicNameValuePair("LastUpdate", String.valueOf(ov.getLastUpdate().toString())));
			
			if(acceptDecline == Job.ACCEPT_JOB){//set statusId =2
				parameters.add(new BasicNameValuePair("StatusId", String.valueOf(ov.getStatusId())));
				parameters.add(new BasicNameValuePair("JobId", String.valueOf(ov.getJobId())));
			}else if(acceptDecline == Job.DECLINE_JOB){//set JobId=null
				parameters.add(new BasicNameValuePair("StatusId", String.valueOf(ov.getStatusId())));
				//JobId null
				parameters.add(new BasicNameValuePair("JobId", "null"));
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
			
			//execute the request
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			return;//return anyway
		}catch(Exception e){
			Log.e(TAG, "Error occured while AcceptDecline " + e);
			return;
		}
	}

}
