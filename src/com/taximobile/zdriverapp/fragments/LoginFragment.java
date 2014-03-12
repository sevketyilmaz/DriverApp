package com.taximobile.zdriverapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.taximobile.zdriverapp.R;

public class LoginFragment extends Fragment{
	private static final String TAG = "LoginFragment";
	
	private EditText userNameEditText, passwordEditText;
	private Button loginButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		
		userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
		passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
		
		loginButton = (Button) view.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO check from server for authentication
				boolean isAuth = true;
				if(isAuth){
					//TODO record his userName and password (sharedPref)
					
					//TODO replace LoginFragment with PositionFragment
					FragmentManager fm = getFragmentManager();
					fm.beginTransaction()
						.replace(R.id.fragmentContainer, new PositionFragment())
						.commit();
				}
			}
		});
		
		return view;
	}
}
