package com.taximobile.zdriverapp.model;

public class LoginModel {
	private String _userName;
	private String _password;
	private String _numberPlate;
	
	//constructors
	public LoginModel(){}
	
	public LoginModel(String userName, String password, String numberPlate)
	{
		setUserName(userName);
		setPassword(password);
		setNumberPlate(numberPlate);
	}

	//getter setters
	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}
	
	public String getNumberPlate(){
		return _numberPlate;
	}
	
	public void setNumberPlate(String numberPlate){
		_numberPlate = numberPlate;
	}
}
