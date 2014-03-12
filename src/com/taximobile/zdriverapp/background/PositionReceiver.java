package com.taximobile.zdriverapp.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class PositionReceiver extends BroadcastReceiver{
	private static final String TAG = "PositionReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		/*if you got a location extra; use it
		 * LocationManager.KEY_LOCATION_CHANGED key used for Bundle extra.
		 * LocationManager broadcast a new PendingIntent when location changed occurs,
		 * this intent has an extra with Location value
		*/
		Location loc = (Location) intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
		if(loc != null){
			onLocationReceived(context, loc);
			return;
		}
		
		//if you get here, something else happened
		if(intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){
			boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
			onProviderEnabledChanged(enabled);
		}
	}

	protected void onLocationReceived(Context context, Location loc) {
		Log.d(TAG, " Got location from " + loc.getProvider() +
				"Lat/Lng " + loc.getLatitude() + " / " + loc.getLongitude());
	}

	protected void onProviderEnabledChanged(boolean enabled) {
		Log.d(TAG, "Provider " + (enabled ? "Enabled" : "Disabled"));
	}

}
