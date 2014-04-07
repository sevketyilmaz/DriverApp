package com.taximobile.zdriverapp.model;

import java.sql.Timestamp;

public class OnlineVehicle {
	private int _vehicleId;
	private int _driverId;
	private int _statusId;
	private double _lat;
	private double _lng;
	private Timestamp _lastUpdate;
	private int _jobId;
	
	//constructor
	public OnlineVehicle()
	{
		setLastUpdate(System.currentTimeMillis());
	}

	public OnlineVehicle(int vehicleId, int driverId, int statusId, double lat,
						double lng, int jobId){
		
		setVehicleId(vehicleId);
		setDriverId(driverId);
		setStatusId(statusId);
		setLat(lat);
		setLng(lng);
		setJobId(jobId);
	}
	
	//getters setter
	public int getVehicleId() {
		return _vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		_vehicleId = vehicleId;
	}

	public int getDriverId() {
		return _driverId;
	}

	public void setDriverId(int driverId) {
		_driverId = driverId;
	}

	public int getStatusId() {
		return _statusId;
	}

	public void setStatusId(int statusId) {
		_statusId = statusId;
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

	public int getJobId() {
		return _jobId;
	}

	public void setJobId(int jobId) {
		_jobId = jobId;
	}
		
	
}
