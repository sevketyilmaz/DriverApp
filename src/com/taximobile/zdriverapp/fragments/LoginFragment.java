package com.taximobile.zdriverapp.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.taximobile.zdriverapp.R;
import com.taximobile.zdriverapp.background.LogOnAsyncTask;
import com.taximobile.zdriverapp.model.*;

public class LoginFragment extends Fragment implements LogOnAsyncTask.ILogOnReadyListener{
	private static final String TAG = "LoginFragment";
	
	private EditText userNameEditText, passwordEditText, numberPlateEditText;
	private Button loginButton;
	
	private FragmentManager fm;
	InputMethodManager inMgr;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		inMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		
		userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
		passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
		numberPlateEditText = (EditText) view.findViewById(R.id.numberPlateEditText);
		
		loginButton = (Button) view.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//TODO check from server for authentication
				if ((!userNameEditText.getText().toString().matches("")) &&
					(!passwordEditText.getText().toString().matches("")) &&
					(!numberPlateEditText.getText().toString().matches(""))) {
					//checking if user is authenticated
					checkPassword(userNameEditText.getText(),
								passwordEditText.getText(),
								numberPlateEditText.getText());
				}			
				userNameEditText.requestFocus();
			}
		});
		
		return view;
	}
	
	//callback method from LogOnAsyncTask.LogOnReadyListener
	public void LogOnReady(){
		Driver driver = ModelManager.Get().getDriver();
		if(driver != null){
			//remove the softkeypad
			inMgr.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
			
			fm = getFragmentManager();
			fm.beginTransaction()
				.replace(R.id.fragmentContainer, new ScreenFragment())
				.commit();
		}else{
			userNameEditText.setText("");
			numberPlateEditText.setText("");
			passwordEditText.setText("");
		}
	}

	
	private void checkPassword(Editable uName, Editable pass, Editable numPlate){
		LoginModel loginModel = new LoginModel(uName.toString(), pass.toString(), numPlate.toString());
		ModelManager.Get().setLoginModel(loginModel);
		LoginModel l = ModelManager.Get().getLoginModel();
		LogOnAsyncTask task = new LogOnAsyncTask(getActivity(), this);
		task.execute();
	}
}
