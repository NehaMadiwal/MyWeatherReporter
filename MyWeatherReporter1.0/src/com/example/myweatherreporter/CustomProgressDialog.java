package com.example.myweatherreporter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class CustomProgressDialog extends ProgressDialog{
	
	private Context context;
	private int dialogType;
	
	public static final int SEARCH_LOCATION=0;
	public static final int ASYNC_TASK_LOAD_CURRENT_WEATHER=1;
	public static final int ASYNC_TASK_LOAD_HISTORY=2;

	public CustomProgressDialog(Context context, int dialogType) {
		super(context);
		this.context=context;
		this.dialogType=dialogType;
	}

	@Override
	public void onBackPressed() {
		Log.d("CustomProgressDialog","onBackPressed");
		switch(this.dialogType){
		case SEARCH_LOCATION:
			if(this.isShowing()){
				Log.d("CustomProgressDialog","search dialog showing");
				AddCityActivity activity=(AddCityActivity)context;
				activity.dismissSearchDialog();
			}
			break;
		case ASYNC_TASK_LOAD_CURRENT_WEATHER:
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
		super.onBackPressed();
	}
	

}
