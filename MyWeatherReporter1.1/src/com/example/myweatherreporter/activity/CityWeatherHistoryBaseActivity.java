package com.example.myweatherreporter.activity;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.myweatherreporter.application.MyWeatherReporter;
import com.example.myweatherreporter.module.CityHistoryActivityModule;

import dagger.ObjectGraph;

public class CityWeatherHistoryBaseActivity extends Activity{
	
	private ObjectGraph activityGraph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("CityWeatherHistoryBaseActivity","onCreate()");

		MyWeatherReporter myWeatherReporter = (MyWeatherReporter)getApplication();
		activityGraph=myWeatherReporter.getApplicationGraph().plus(getModules().toArray());
	
		activityGraph.inject(this);
	}
	
	@Override protected void onDestroy() {
		activityGraph = null;
		super.onDestroy();
	}

	protected List<Object> getModules() {
		return Arrays.<Object>asList(new CityHistoryActivityModule(this));
	}

}
