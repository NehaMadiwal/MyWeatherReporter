package com.example.myweatherreporter.jsonhelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.myweatherreporter.beans.CityHistoryOneDay;
import com.example.myweatherreporter.beans.CityWeather;

public class JsonCityWeatherParser {
	
	//get jsondata from "data" and assign to "cityWeather"
		public static CityWeather getCityWeather(String data,CityWeather cityWeather) throws JSONException{
			
			JSONObject jObj=new JSONObject(data);
			
			
			JSONObject sysObj=getObject("sys",jObj);
			
			cityWeather.getCity().setCityName(getString("name",jObj));
			cityWeather.getCity().setCountry(getString("country",sysObj));
			
			JSONObject mainObj = getObject("main", jObj);
			cityWeather.setTemp(getFloat("temp", mainObj));
			cityWeather.setTempMax(getFloat("temp_max", mainObj));
			cityWeather.setTempMin(getFloat("temp_min", mainObj));
			
			
			JSONArray jArr = jObj.getJSONArray("weather");
	        JSONObject jSONWeather = jArr.getJSONObject(0);
	        cityWeather.setWeatherDescription(getString("description", jSONWeather));
	        cityWeather.setIcon(getString("icon", jSONWeather));
	        
	        int id= getInt("id",jObj);
	        cityWeather.setJsonCityId(id);
	        Log.d("parser","jsoncityid="+id);
	        
	        return cityWeather;
			
		}

		//get weather history from "data" and assign to "cityHistoryOneDay"
		public static CityHistoryOneDay getCityWeatherHistory(String data, CityHistoryOneDay cityHistoryOneDay) throws JSONException{
			
			JSONObject jObj=new JSONObject(data);
			JSONArray jSONList=jObj.getJSONArray("list");
			
			JSONObject jSONOneDay=jSONList.getJSONObject(0);
			JSONArray jSONWeatherArr=jSONOneDay.getJSONArray("weather");
			JSONObject jSONWeather=jSONWeatherArr.getJSONObject(0);
			cityHistoryOneDay.setWeatherDescription(getString("description",jSONWeather));
			cityHistoryOneDay.setIcon(getString("icon", jSONWeather));
			
			JSONObject mainObj = getObject("main", jSONOneDay);
			cityHistoryOneDay.setTemp(getFloat("temp",mainObj));
			cityHistoryOneDay.setTempMax(getFloat("temp_max",mainObj));
			cityHistoryOneDay.setTempMin(getFloat("temp_min",mainObj));
			
			return cityHistoryOneDay;
			
		}


		private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
			JSONObject subObj = jObj.getJSONObject(tagName);
			return subObj;
		}

		private static String getString(String tagName, JSONObject jObj) throws JSONException {
			return jObj.getString(tagName);
		}

		private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
			return (float) jObj.getDouble(tagName);
		}

		private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
			return jObj.getInt(tagName);
		}


}
