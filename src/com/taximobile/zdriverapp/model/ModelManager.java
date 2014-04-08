package com.taximobile.zdriverapp.model;

/*
 * Singleton
 * */
public class ModelManager {
	private static final String TAG = "ModelManager";
	
	private static ModelManager _modelManager = null;
	
	private LoginModel _loginModel;
	private Driver _driver;
	private OnlineVehicle _onlineVehicle;
	private Customer _customer;
	private Job _job;
	
	//constructor
	private ModelManager(){
//		_loginModel = new LoginModel();
//		_driver = new Driver();
//		_onlineVehicle = new OnlineVehicle();
//		_customer = new Customer();
//		_job = new Job();
	}
	
	public static ModelManager Get(){
		if(_modelManager == null)
			_modelManager = new ModelManager();
		
		return _modelManager;
	}
	
	//getter setters
	public LoginModel getLoginModel(){
		return _loginModel;
	}
	
	public void setLoginModel(LoginModel loginModel){
		_loginModel = loginModel;
	}
	
	public Driver getDriver(){
		return _driver;
	}
	
	public void setDriver(Driver driver){
		_driver = driver;
	}
	
	public OnlineVehicle getOnlineVehicle(){
		return _onlineVehicle;
	}
	
	public void setOnlineVehicle(OnlineVehicle onlineVehicle){
		_onlineVehicle = onlineVehicle;
	}
	
	public Customer getCustomer(){
		return _customer;
	}
	
	public void setCustomer(Customer customer){
		_customer = customer;
	}
	
	public Job getJob(){
		return _job;
	}
	
	public void setJob(Job job){
		_job = job;
	}
}
