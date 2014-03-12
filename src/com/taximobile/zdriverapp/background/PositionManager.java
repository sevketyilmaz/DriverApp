package com.taximobile.zdriverapp.background;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/*Singleton class
 * */
public class PositionManager {
	public static final String ACTION_LOCATION = "com.taximobile.zdriverapp.ACTION_LOCATION";
	public static final int APP_TYPE_CUSTOMER = 89;
	public static final int APP_TYPE_DRIVER = 97;
	private static final long MIN_TIME_INTERVAL = 0;
	
	private static PositionManager _positionManager;
	private Context _appContext;
	private LocationManager _locationManager;

	private int _appType;
	
	private PositionManager(Context appContext, int appType){
		_appContext = appContext;
		_appType = appType;
		_locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public static PositionManager Get(Context c, int appType){
		if(_positionManager == null){
			_positionManager = new PositionManager(c.getApplicationContext(), appType);
		}		
		return _positionManager;
	}
	
	private PendingIntent getLocationPendingIntent(boolean shouldCreate){
		Intent broadcastIntent = new Intent(ACTION_LOCATION);
		int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
		return PendingIntent.getBroadcast(_appContext, 0, broadcastIntent, flags);
	}
	
	public void startLocationUpdates(){
		if(_appType == APP_TYPE_CUSTOMER){
			String provider = LocationManager.GPS_PROVIDER;
			PendingIntent pi = getLocationPendingIntent(true);
			_locationManager.requestSingleUpdate(provider, pi);
			
		}else if(_appType == APP_TYPE_DRIVER){
			String provider = LocationManager.GPS_PROVIDER;
			PendingIntent pi = getLocationPendingIntent(true);
			_locationManager.requestLocationUpdates(provider, MIN_TIME_INTERVAL, 0, pi);
		}
	}
	
	public void stopLocationUpdates(){
		PendingIntent pi = getLocationPendingIntent(false);
		if(pi != null){
			_locationManager.removeUpdates(pi);
			pi.cancel();
		}
	}
	
	public boolean isTrackingPosition(){
		return getLocationPendingIntent(false) != null;
	}
}
