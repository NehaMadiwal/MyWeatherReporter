package com.example.myweatherreporter.widget;

import javax.inject.Inject;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.example.myweatherreporter.activity.AddCityActivity;
import com.example.myweatherreporter.activity.CityWeatherHistoryActivity;

public class CustomProgressDialog extends ProgressDialog{

	private Context context;
	int dialogType;
	
	
	public static final int SEARCH_LOCATION=0;
	public static final int ASYNC_TASK_LOAD_CURRENT_WEATHER=1;
	public static final int ASYNC_TASK_LOAD_HISTORY=2;
	
	private static final String TAG="CustomProgressDialog";

	
	
	@Inject
	public CustomProgressDialog(Context context){
		super(context);
		this.context=context;
		
		this.setProgressStyle(ProgressDialog.STYLE_SPINNER); //default
		this.setCancelable(false);
		this.setIndeterminate(false);
		
		Log.d(TAG, "dialog constructor");
	}
	
	public int getDialogType() {
		return dialogType;
	}


	public void setDialogType(int dialogType) {
		this.dialogType = dialogType;
	}
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG, "onBackPressed");

		switch(this.dialogType){
		case SEARCH_LOCATION:
			if(this.isShowing()){
				Log.d("CustomProgressDialog","search dialog showing");
				AddCityActivity activity=(AddCityActivity)context;
				activity.dismissSearchDialog();
			}
			break;
		case ASYNC_TASK_LOAD_CURRENT_WEATHER :
			if(this.isShowing()){
				Log.d("CustomProgressDialog","async task dialog showing");
				AddCityActivity activity=(AddCityActivity)context;
				activity.dismissLoadListDialog();
			}
			break;
		case ASYNC_TASK_LOAD_HISTORY:
			if(this.isShowing()){
				Log.d("CustomProgressDialog","async history task dialog showing");
				CityWeatherHistoryActivity activity=(CityWeatherHistoryActivity)context;
				activity.cancelAsyncTasks();
			}

		}
	}
}