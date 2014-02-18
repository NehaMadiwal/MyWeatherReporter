package com.example.myweatherreporter.activity;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.example.myweatherreporter.application.MyWeatherReporter;
import com.example.myweatherreporter.module.AddCityActivityModule;

import dagger.ObjectGraph;

public class AddCityBaseActivity extends Activity {

	private ObjectGraph activityGraph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MyWeatherReporter myWeatherReporter = (MyWeatherReporter)getApplication();
		activityGraph=myWeatherReporter.getApplicationGraph().plus(getModules().toArray());
	
		activityGraph.inject(this);
	}

	@Override protected void onDestroy() {
		activityGraph = null;
		super.onDestroy();
	}

	protected List<Object> getModules() {
		return Arrays.<Object>asList(new AddCityActivityModule(this));
	}

	
}