package com.taximobile.zdriverapp.model;

/*
 * Singleton
 * */
public class ModelManager {
	private static final String TAG = "ModelManager";
	
	private static ModelManager _modelManager = null;
	
	private LoginModel _loginModel;
	
	//constructor
	private ModelManager(){
		_loginModel = new LoginModel();
	}
	
	public static ModelManager Get(){
		if(_modelManager == null)
			return new ModelManager();
		
		return _modelManager;
	}
	
	//getter setters
	public LoginModel getLoginModel(){
		return _loginModel;
	}
	
	public void setLoginModel(LoginModel loginModel){
		_loginModel = loginModel;
	}
}
