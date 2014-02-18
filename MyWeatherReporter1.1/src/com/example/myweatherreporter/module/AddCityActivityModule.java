package com.example.myweatherreporter.module;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Singleton;

import android.content.Context;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;

import com.example.myweatherreporter.R;
import com.example.myweatherreporter.activity.AddCityActivity;
import com.example.myweatherreporter.activity.AddCityBaseActivity;
import com.example.myweatherreporter.beans.CityWeather;
import com.example.myweatherreporter.listadapter.CustomCityListAdapter;

import dagger.Module;
import dagger.Provides;

@Module(
		injects = {
				AddCityActivity.class,
		},
		//addsTo = MyWeatherReporterModule.class,
		library = true,
		complete=true
		)
public class AddCityActivityModule {

	private final AddCityBaseActivity activity;

	public AddCityActivityModule(AddCityBaseActivity activity) {
		this.activity = activity;
	}

	@Provides
	@Singleton
	public Context provideActivityContext()
	{
		return this.activity;
	}
	
	@Provides
	@Singleton 
	public LocationManager provideLocationManager(){
		
		return (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		
	}
	@Provides
	@Singleton 
	public Geocoder provideGeocoder(){
		return new Geocoder(activity, Locale.getDefault());
	}
	
	@Provides
	@Singleton
	public ArrayList<CityWeather> provideCityWeatherArrayList(){
		Log.d("ActivityModule", "provideCityWeatherList");
		return new ArrayList<CityWeather>();
	}
	
	@Provides 
	public CustomCityListAdapter provideCustomCityListAdapter(ArrayList<CityWeather> cityWeatherList){
		Log.d("ActivityModule", "provideCustomCityListAdapter");
		return new CustomCityListAdapter(activity,R.layout.list_item,cityWeatherList);
	}
	
	/*@Provides
	public CustomProgressDialog provideProgressDialog(Context context){
		return new CustomProgressDialog(context);
		
	}*/
	
	/*@Provides City provideCity(){
		return new City();
	}*/
	
	
}
