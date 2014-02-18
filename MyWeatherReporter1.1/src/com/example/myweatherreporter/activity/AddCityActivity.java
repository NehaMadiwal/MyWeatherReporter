package com.example.myweatherreporter.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myweatherreporter.R;
import com.example.myweatherreporter.beans.City;
import com.example.myweatherreporter.beans.CityWeather;
import com.example.myweatherreporter.db.DatabaseHandler;
import com.example.myweatherreporter.jsonhelper.JsonCityWeatherParser;
import com.example.myweatherreporter.jsonhelper.JsonHttpClient;
import com.example.myweatherreporter.listadapter.CustomCityListAdapter;
import com.example.myweatherreporter.util.NetworkUtils;
import com.example.myweatherreporter.widget.CustomProgressDialog;

public class AddCityActivity extends AddCityBaseActivity implements OnClickListener,OnItemClickListener,OnItemLongClickListener,LocationListener{

	private static final String TAG="AddCityActivity";
	private Button btnCurrentLocation;
	private Button btnAddCity;
	private Button btnHeader;
	
	private AutoCompleteTextView atvCity;
	
	private ListView lv;
	
	@Inject
	ArrayList<CityWeather> cityWeatherList;
	
	@Inject
	CustomCityListAdapter adapter;
	
	private  LoadCityWeatherList loadCityWeatherList=null;
	
	
	@Inject	City city;
	@Inject Provider<CityWeather> cityWeatherProvider;
	
	
	@Inject CustomProgressDialog progressDialog;

	@Inject DatabaseHandler dbHandler;
	@Inject	LocationManager locationManager;
	@Inject	Geocoder geocoder;
	
	
	private ActionMode mActionMode=null ;
	private int selectedListItem=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_city);
		Log.d(TAG,"onCreate()");

		btnCurrentLocation = (Button)findViewById(R.id.btnCurrentCity);
		btnAddCity = (Button) findViewById(R.id.btnAddCity);
		btnHeader = (Button) findViewById(R.id.btnHeaderCityList);
		
		btnCurrentLocation.setOnClickListener(this);
		btnAddCity.setOnClickListener(this);
		
		atvCity = (AutoCompleteTextView) findViewById(R.id.atvCity);
		
		lv=(ListView)findViewById(R.id.listCity);
	    lv.setAdapter(adapter);
	    
	    lv.setOnItemClickListener(this);
	    lv.setOnItemLongClickListener(this);
		
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart()");
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume()");
		
		if(locationManager!=null){
			Log.d(TAG,"onResume: remove update");
			locationManager.removeUpdates(this);
		}
		loadList();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause()");
		dismissSearchDialog();
		dismissLoadListDialog();
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop()");
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy()");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_city, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_refresh :
			loadList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		String cityName=((TextView) view.findViewById(R.id.tvCityName)).getText().toString();
		String cityCountry=((TextView) view.findViewById(R.id.tvCountry)).getText().toString();
		Intent in = new Intent(getApplicationContext(), CityWeatherHistoryActivity.class);
		in.putExtra("cityName", cityName);
		in.putExtra("cityCountry", cityCountry);

		view.setSelected(true);
		startActivity(in);
		
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		
		if (mActionMode != null) {
            return false;
        }
		selectedListItem=position;
		
		view.setSelected(true);
		mActionMode =startActionMode(mActionModeCallback);
		return true;
	}
	
	@Override
	public void onClick(View view) {

		switch(view.getId()){
		case R.id.btnAddCity:
			hideVirualKeyBoard();
			
			if(atvCity.getText().toString().equals(""))
				Toast.makeText(getBaseContext(),getResources().getString(R.string.input_city),Toast.LENGTH_LONG).show();
			else{
				addCity(atvCity.getText().toString());
			}
			atvCity.setText("");
			
			break;
		case R.id.btnCurrentCity:
			if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				progressDialog.setMessage(getResources().getString(R.string.searching));
				progressDialog.setDialogType(CustomProgressDialog.SEARCH_LOCATION);
				progressDialog.show();
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, this);
			}
			else{
				Toast.makeText(this, getResources().getString(R.string.gps_off), Toast.LENGTH_LONG).show();
				showGpsDisabledAlert();
			}
			break;	

		}
	}
	
	
	//updates CityWeather list with no data for weather for all cities in db
	public void updateCityWeatherList(){
		
		ArrayList<City> cityList=dbHandler.getAllCities();
		for(City city:cityList){
			CityWeather cityWeather= cityWeatherProvider.get();
			cityWeather.setCity(city);
			cityWeatherList.add(cityWeather);
			Log.d("city in cityweather",cityWeather.getCity().getCityName());
		}
	}

	
	public  boolean isLoadCityWeatherListFinished(){
		return (loadCityWeatherList==null || loadCityWeatherList.getStatus() == AsyncTask.Status.FINISHED);
	}
	
	
	//start async task
	public void loadList(){
		
		Log.d(TAG, "loadlist()");
		
		if(dbHandler.getCityCount()==0){
			btnHeader.setText(getResources().getString(R.string.header_no_city));
			cityWeatherList.clear();
			adapter.notifyDataSetChanged();
		}else if(!NetworkUtils.isInternetAvailable(this)){
			btnHeader.setText(getResources().getString(R.string.no_internet));
			Toast.makeText(this,getResources().getString(R.string.no_internet),Toast.LENGTH_LONG).show();
			cityWeatherList.clear();
			updateCityWeatherList();
			adapter.notifyDataSetChanged();
		}else{
			btnHeader.setText(getResources().getString(R.string.header_current_city_weather));
			if(isLoadCityWeatherListFinished()){
				loadCityWeatherList=new LoadCityWeatherList();
				cityWeatherList.clear();
				updateCityWeatherList();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					loadCityWeatherList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				else
					loadCityWeatherList.execute();
			}
			
		}

	}
	
	//add city to db
	public void addCity(String cityName){
		city.setCityName(cityName);
		boolean duplicateCity=dbHandler.addCity(city);
		if(duplicateCity)
			Toast.makeText(getBaseContext(),getResources().getString(R.string.city_present),Toast.LENGTH_LONG).show();
		else{
			Toast.makeText(getBaseContext(),getResources().getString(R.string.city_added),Toast.LENGTH_LONG).show();
			loadList();
		}
	}
	
	
	//delete city from db
		public void deleteCity(int pos){
			CityWeather selectedCityWeather=adapter.getItem(pos);
			
			dbHandler.deleteCity(selectedCityWeather.getCity());
			Toast.makeText(getBaseContext(),selectedCityWeather.getCity().getCityName()+getResources().getString(R.string.city_deleted),Toast.LENGTH_LONG).show();
			selectedListItem=-1;
			loadList();
		}
	
	
	//show dialog if GPS is off
	private void showGpsDisabledAlert(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(getResources().getString(R.string.gps_disabled_msg))
		.setCancelable(false)
		.setTitle(getResources().getString(R.string.gps_title))
		.setPositiveButton(getResources().getString(R.string.goto_settings),
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent callGPSSettingsIntent=new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(callGPSSettingsIntent);

			}
		});
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		AlertDialog alert=alertDialogBuilder.create();
		alert.show();
	}
		
		
	//hide virual keyboard
	public void hideVirualKeyBoard(){

		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	//cancel gps city search
	public void dismissSearchDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			Log.d(TAG,"dismiss search dialog");
			locationManager.removeUpdates(this);
			progressDialog.dismiss();
		}
	}
	
	//Cancel async task
	public void dismissLoadListDialog(){
		if(!isLoadCityWeatherListFinished()){
			Log.d("AddCityActivity","task not finished");
			loadCityWeatherList.cancel(true);
		}
	}

	

	@Override
	public void onLocationChanged(Location location) {
		progressDialog.dismiss();
		Toast.makeText(AddCityActivity.this,getResources().getString(R.string.latitude) + location.getLatitude() +"\n"
				+ getResources().getString(R.string.longitude) + location.getLongitude(),Toast.LENGTH_LONG).show();
		String cityName=null;
		
		List<Address> addresses;
		
		try {
			addresses= geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			Toast.makeText(AddCityActivity.this, "after address", Toast.LENGTH_LONG).show();
			if(addresses.size()>0){
				Toast.makeText(AddCityActivity.this, "address > 0", Toast.LENGTH_LONG).show();
				cityName=addresses.get(0).getLocality(); 
				if(cityName!=null){
					Toast.makeText(AddCityActivity.this, cityName, Toast.LENGTH_LONG).show();
					addCity(cityName);
					locationManager.removeUpdates(this);
				}
			}
		} catch (IOException e) {
			Toast.makeText(AddCityActivity.this, "City not found", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
	}
	@Override
	public void onProviderDisabled(String arg0) {}
	@Override
	public void onProviderEnabled(String arg0) {}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
	
	
	
	//CAB 
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback(){

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				//Toast.makeText(getBaseContext(),"Delete CAB clicked",Toast.LENGTH_LONG).show();
				deleteCity(selectedListItem);
				mode.finish();
				return true;

			default:
				return false;
			}

		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.context_menu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode arg0) {
			mActionMode = null;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
			return false;
		}

	};

	//async task for city weather
	private class LoadCityWeatherList extends AsyncTask<Void,Void,Void>{
		CustomProgressDialog progressDialog=null;
		@Override
		protected void onPreExecute() {

			Log.d("Async task", "onpreexecute");
			
			progressDialog = new CustomProgressDialog(AddCityActivity.this);
			progressDialog.setDialogType(CustomProgressDialog.ASYNC_TASK_LOAD_CURRENT_WEATHER);
			progressDialog.setMessage(getResources().getString(R.string.loading));
			progressDialog.show();
			
		}
		@Override
		protected Void doInBackground(Void... arg0){

			for(CityWeather cityWeather:cityWeatherList){
				if(!isCancelled()){
					
					String name=cityWeather.getCity().getCityName();
					Log.d(cityWeather.getCity().getCityName(),""+cityWeather.getCity().getId());
					String jsonData=( (new JsonHttpClient()).getCityWeatherData(name));
					
					if(jsonData!=null){
						try{
							
							//Log.d("jsondata",jsonData);
							cityWeather=JsonCityWeatherParser.getCityWeather(jsonData,cityWeather);
							cityWeather.setDataAvailable(true);
						}catch(JSONException e){
							cityWeather.setDataAvailable(false);
							Log.d("json exception", "json exception");
							e.printStackTrace();
						}
					}
					else
						cityWeather.setDataAvailable(false);
				}
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.d("async task load list", "on postexecute");
			
			progressDialog.dismiss();
									
			adapter.notifyDataSetChanged();
		}
		
		@Override
		protected void onCancelled() {
			Log.d("async task load list", "on cancelled");
			
			progressDialog.dismiss();
			
			cityWeatherList.clear();
			updateCityWeatherList();
			adapter.notifyDataSetChanged();
		}
		
	}

}
