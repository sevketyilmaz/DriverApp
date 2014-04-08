package com.taximobile.zdriverapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.taximobile.zdriverapp.fragments.LoginFragment;
import com.taximobile.zdriverapp.model.ModelManager;

public class MainActivity extends Activity {
	public static final String TM_BASE_URL = "http://svktylmz-001-site1.myasp.net/";
	public static final String TM_LOGON_DRIVER = TM_BASE_URL + "api/Logon/drivervehicle"; //POST
	public static final String TM_POSITION_PUSH = TM_BASE_URL + "api/onlinevehicle"; //PUT


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		ModelManager mm = ModelManager.Get();
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment  = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null){
			fragment = new LoginFragment();
			fm.beginTransaction()
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
