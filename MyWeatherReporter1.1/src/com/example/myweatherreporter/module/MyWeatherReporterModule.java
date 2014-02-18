package com.example.myweatherreporter.module;

import javax.inject.Singleton;

import android.content.Context;

import com.example.myweatherreporter.application.MyWeatherReporter;

import dagger.Module;
import dagger.Provides;

@Module(
		injects={
				//AddCityActivity.class
		},
		library=true,
		complete=true
		)
public class MyWeatherReporterModule {
	
	private MyWeatherReporter application;
	private Context context;
	
	public MyWeatherReporterModule(MyWeatherReporter myWeatherReporter){
		this.application = myWeatherReporter;
		context = this.application.getApplicationContext();
	}
	
	@Provides
	@Singleton
	public Context provideApplicationContext()
	{
		return this.context;
	}
	
	
	
}
