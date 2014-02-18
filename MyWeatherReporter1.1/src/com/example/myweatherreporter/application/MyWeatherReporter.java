package com.example.myweatherreporter.application;

import java.util.Arrays;
import java.util.List;

import com.example.myweatherreporter.module.MyWeatherReporterModule;

import dagger.ObjectGraph;
import android.app.Application;

public class MyWeatherReporter extends Application{

	private ObjectGraph applicationGraph;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Object[] modules = getModules().toArray();
		applicationGraph= ObjectGraph.create(modules);
	}
	
	protected List<Object> getModules(){
		return Arrays.<Object>asList(
				new MyWeatherReporterModule(this)
				);
	}
	
	public ObjectGraph getApplicationGraph(){
		return this.applicationGraph;
	}
	

}
