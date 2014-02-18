package com.example.myweatherreporter.beans;

import javax.inject.Inject;

import android.util.Log;

public class CityHistoryOneDay {

	private static final String TAG="CityHistoryOneDay";

	private String date;
	private String day;

	private String weatherDescription;
	private String icon;

	private float temp;
	private float tempMin;
	private float tempMax;

	private boolean dataAvailable=false;

	@Inject
	public CityHistoryOneDay(){
		Log.d(TAG, "CityHistoryOneDay constructor");
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getWeatherDescription() {
		return weatherDescription;
	}

	public void setWeatherDescription(String weatherDescription) {
		this.weatherDescription = weatherDescription;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public float getTemp() {
		return temp;
	}

	public void setTemp(float temp) {
		this.temp = temp;
	}

	public float getTempMin() {
		return tempMin;
	}

	public void setTempMin(float tempMin) {
		this.tempMin = tempMin;
	}

	public float getTempMax() {
		return tempMax;
	}

	public void setTempMax(float tempMax) {
		this.tempMax = tempMax;
	}

	public boolean isDataAvailable() {
		return dataAvailable;
	}

	public void setDataAvailable(boolean dataAvailable) {
		this.dataAvailable = dataAvailable;
	}

}
