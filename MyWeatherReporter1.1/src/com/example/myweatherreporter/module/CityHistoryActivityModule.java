package com.example.myweatherreporter.module;

import java.util.ArrayList;

import javax.inject.Singleton;

import android.content.Context;
import android.util.Log;

import com.example.myweatherreporter.R;
import com.example.myweatherreporter.activity.CityWeatherHistoryActivity;
import com.example.myweatherreporter.activity.CityWeatherHistoryBaseActivity;
import com.example.myweatherreporter.beans.CityHistoryOneDay;
import com.example.myweatherreporter.listadapter.CustomCityHistoryListAdapter;

import dagger.Module;
import dagger.Provides;


@Module(
		injects = {
				CityWeatherHistoryActivity.class,
		},
		//addsTo = MyWeatherReporterModule.class,
		library = true,
		complete=true
		)
public class CityHistoryActivityModule {
	
	private final CityWeatherHistoryBaseActivity activity;
	
	public CityHistoryActivityModule(CityWeatherHistoryBaseActivity activity) {
		this.activity = activity;
	}
	
	@Provides
	@Singleton
	public Context provideCityHistoryActivityContext()
	{
		return this.activity;
	}
	
	@Provides
	@Singleton
	public ArrayList<CityHistoryOneDay> provideCityWeatherHistoryArrayList(){
		Log.d("CityHistoryActivityModule", "provideCityWeatherHistoryList");
		return new ArrayList<CityHistoryOneDay>();
	}
	
	@Provides
	public CustomCityHistoryListAdapter provideCustomCityListAdapter(ArrayList<CityHistoryOneDay> cityHistoryList){
		Log.d("CityHistoryActivityModule", "provideCustomCityHistoryListAdapter");
		return new CustomCityHistoryListAdapter(activity,R.layout.history_list_item,cityHistoryList);
	}
	

}
