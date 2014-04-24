package com.taximobile.zdriverapp.model;

import java.sql.Timestamp;

public class Customer {
	private int _customerId;
	private String _customerName;
	private String _email;
	private String _gsm;
	private double _lat;
	private double _lng;
	private Timestamp _lastUpdate;
	
	//constructor
	public Customer(){
		setLastUpdate(System.currentTimeMillis());
	}
	
	public Customer(int customerId, double lat, double lng){
		this();
		setCustomerId(customerId);
		setLat(lat);
		setLng(lng);
	}

	public Customer(int customerId, String customerName, String email, String gsm,
				double lat, double lng){
		this();
		setCustomerId(customerId);
		setCustomerName(customerName);
		setEmail(email);
		setGsm(gsm);
		setLat(lat);
		setLng(lng);
	}
	
	public Customer(int customerId, String customerName, String email, String gsm,
			double lat, double lng, String lastUpdate){
	setCustomerId(customerId);
	setCustomerName(customerName);
	setEmail(email);
	setGsm(gsm);
	setLat(lat);
	setLng(lng);
	
	setLastUpdate(lastUpdate);
}
	
	//getters setters
	public int getCustomerId() {
		return _customerId;
	}

	public void setCustomerId(int customerId) {
		_customerId = customerId;
	}

	public String getCustomerName() {
		return _customerName;
	}

	public void setCustomerName(String customerName) {
		_customerName = customerName;
	}

	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		_email = email;
	}

	public String getGsm() {
		return _gsm;
	}

	public void setGsm(String gsm) {
		_gsm = gsm;
	}

	public double getLat() {
		return _lat;
	}

	public void setLat(double lat) {
		_lat = lat;
	}

	public double getLng() {
		return _lng;
	}

	public void setLng(double lng) {
		_lng = lng;
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
