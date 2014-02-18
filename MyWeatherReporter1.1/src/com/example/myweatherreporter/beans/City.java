package com.example.myweatherreporter.beans;

import javax.inject.Inject;

import android.util.Log;

public class City {

	private int id;
	private String cityName;
	private String country;

	@Inject
	public City(){
		Log.d("City", "city constructor");
	}

	public City(String cityName){
		this.cityName = cityName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
