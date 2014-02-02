package com.example.jsonhelper;

import com.example.myweatherreporterdb.City;

public class CityWeather {
	
	private City city;
	
	private int jsonCityId;
	
	private float temp;
	private float tempMin;
	private float tempMax;
	
	private String weatherDescription;
	private String icon;
	
	public byte[] iconData;
	
	private boolean dataAvailable=false;
	
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public int getJsonCityId() {
		return jsonCityId;
	}
	public void setJsonCityId(int jsonCityId) {
		this.jsonCityId = jsonCityId;
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
	
	public boolean isDataAvailable() {
		return dataAvailable;
	}
	public void setDataAvailable(boolean dataAvailable) {
		this.dataAvailable = dataAvailable;
	}
	
}
