package com.taximobile.zdriverapp.model;

import java.sql.Timestamp;

public class Driver {
	private int _driverId;
	private String _driverName;
	private String _email;
	private String _password;
	private String _gsm;
	private String _driverLicence;
	
	private int _vehicleId;
	private String _numberPlate;
	private Timestamp _lastUpdate;
	
	//constructor
	public Driver()
	{}
	
	public Driver(int driverId, String driverName, String email,
				String password, String gsm, String driverLicence, String lastUpdate){
			
		setDriverId(driverId);
		setDriverName(driverName);
		setEmail(email);
		setPassword(password);
		setGsm(gsm);
		setDriverLicence(driverLicence);
		setLastUpdate(lastUpdate);
	}
	
	public Driver(int driverId, String driverName, String email,
				String password, String gsm, String driverLicence){
		
		setDriverId(driverId);
		setDriverName(driverName);
		setEmail(email);
		setPassword(password);
		setGsm(gsm);
		setDriverLicence(driverLicence);
		setLastUpdate(System.currentTimeMillis());
	}

	//getters setters
	public int getDriverId() {
		return _driverId;
	}

	public void setDriverId(int driverId) {
		_driverId = driverId;
	}

	public String getDriverName() {
		return _driverName;
	}

	public void setDriverName(String driverName) {
		_driverName = driverName;
	}

	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}

	public String getGsm() {
		return _gsm;
	}

	public void setGsm(String gsm) {
		_gsm = gsm;
	}

	public String getDriverLicence() {
		return _driverLicence;
	}

	public void setDriverLicence(String driverLicence) {
		_driverLicence = driverLicence;
	}

	public int getVehicleId() {
		return _vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		_vehicleId = vehicleId;
	}

	public String getNumberPlate() {
		return _numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		_numberPlate = numberPlate;
	}

	public Timestamp getLastUpdate() {
		return _lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		String s = lastUpdate.replace('T', ' ');
		_lastUpdate = Timestamp.valueOf(s);
	}
	
	public void setLastUpdate(Long lastUpdate){
		_lastUpdate = new Timestamp(lastUpdate);
	}
	

	
}
