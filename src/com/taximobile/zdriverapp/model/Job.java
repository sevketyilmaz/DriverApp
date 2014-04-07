package com.taximobile.zdriverapp.model;

import java.sql.Timestamp;

public class Job {
	private int _jobId;
	private int _driverId;
	private int _vehicleId;
	private int _customerId;
	private Timestamp _requestDate;
	
	//constructor
	public Job(){
		setRequestDate(System.currentTimeMillis());
	}

	public Job(int jobId, int driverId, int vehicleId, int customerId){
		this();
		
		setJobId(jobId);
		setDriverId(driverId);
		setVehicleId(vehicleId);
		setCustomerId(customerId);
	}
	
	public Job(int jobId, int driverId, int vehicleId, int customerId, String requestDate){
		setJobId(jobId);
		setDriverId(driverId);
		setVehicleId(vehicleId);
		setCustomerId(customerId);
		
		setRequestDate(requestDate);
	}

	//getters setters
	public int getJobId() {
		return _jobId;
	}

	public void setJobId(int jobId) {
		_jobId = jobId;
	}

	public int getDriverId() {
		return _driverId;
	}

	public void setDriverId(int driverId) {
		_driverId = driverId;
	}

	public int getVehicleId() {
		return _vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		_vehicleId = vehicleId;
	}

	public int getCustomerId() {
		return _customerId;
	}

	public void setCustomerId(int customerId) {
		_customerId = customerId;
	}

	public Timestamp getRequestDate() {
		return _requestDate;
	}

	public void setRequestDate(String requestDate) {
		String s = requestDate.replace('T', ' ');
		_requestDate = Timestamp.valueOf(s);
	}
	
	public void setRequestDate(Long requestDate){
		_requestDate = new Timestamp(requestDate);
	}
	
	
}
