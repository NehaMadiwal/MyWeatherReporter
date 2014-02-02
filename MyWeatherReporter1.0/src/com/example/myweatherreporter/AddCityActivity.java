package com.example.myweatherreporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jsonhelper.JsonCityWeatherParser;
import com.example.jsonhelper.JsonHttpClient;
import com.example.myweatherreporteradapter.CityListAdapter;
import com.example.myweatherreporterbeans.City;
import com.example.myweatherreporterbeans.CityWeather;
import com.example.myweatherreporterdb.DatabaseHandler;
import com.example.util.NetworkUtil;

public class AddCityActivity extends Activity {

	private CustomProgressDialog progressDialog=null;
	
	private LocationManager locationManager=null;
	private LocationListener locationListener=null;
	
	private AutoCompleteTextView cityTextView;
	private ListView lv;
	List<CityWeather> cityWeatherList=null;
	CityListAdapter adapter=null;
	
	private DatabaseHandler db;
	
	private  LoadCityWeatherList loadCityWeatherList=null;
	
	ActionMode mActionMode=null ;
	private int selectedListItem=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_city);
		
		Log.d("AddCityActivity","onCreate");
		//this.deleteDatabase("CityDB");
		db=new DatabaseHandler(this);
		
		cityTextView=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
		
		lv=(ListView)findViewById(R.id.list);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		cityWeatherList=new ArrayList<CityWeather>();
		adapter=new CityListAdapter(AddCityActivity.this,R.layout.list_item,cityWeatherList);
	    lv.setAdapter(adapter);
	    
	    lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String cityName=((TextView) view.findViewById(R.id.name)).getText().toString();
				String cityCountry=((TextView) view.findViewById(R.id.country)).getText().toString();
				Intent in = new Intent(getApplicationContext(), CityWeatherHistoryActivity.class);
				in.putExtra("cityName", cityName);
				in.putExtra("cityCountry", cityCountry);
				
				view.setSelected(true);
				startActivity(in);
			}
	    	
	    });
	   
	    lv.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mActionMode != null) {
		            return false;
		        }
				selectedListItem=position;
				
				view.setSelected(true);
				mActionMode =startActionMode(mActionModeCallback);
				return true;
			}
	    	
	    });
	    
	    locationListener = new MyCityLocationListener();
		locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("AddCityActivity","onResume");
		if(locationManager!=null&&locationListener!=null){
			Log.d("onResume","remove update");
			locationManager.removeUpdates(locationListener);
		}
		loadList();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("AddCityActivity","onPause");
		dismissSearchDialog();
		dismissLoadListDialog();
	}
	
	@Override
	protected void onDestroy() {
		Log.d("AddCityActivity","onDestory");
		dismissSearchDialog();
		dismissLoadListDialog();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Log.d("AddCityActivity","onBackPressed");
		super.onBackPressed();
	}
	public void dismissSearchDialog(){
		if(progressDialog!=null && progressDialog.isShowing()){
			Log.d("AddCityActivity","dismiss search dialog");
			locationManager.removeUpdates(locationListener);
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
	

	public void onClickAdd(View view){
		
		hideVirualKeyBoard();
		if(cityTextView.getText().toString().equals(""))
			Toast.makeText(getBaseContext(),getResources().getString(R.string.input_city),Toast.LENGTH_LONG).show();
		else{
			addCity(cityTextView.getText().toString(),"");
		}
		cityTextView.setText("");
	}
	
	//add city to db
	public  void addCity(String name, String country) {
		boolean duplicateCity=db.addCity(new City(name.toLowerCase(),country.toLowerCase()));
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
		Toast.makeText(getBaseContext(),selectedCityWeather.getCity().getName()+getResources().getString(R.string.city_deleted),Toast.LENGTH_LONG).show();
		db.deleteCity(selectedCityWeather.getCity());
		selectedListItem=-1;
		loadList();
	}
	//check whether async task is finished or not
	public  boolean isLoadCityWeatherListFinished(){
		return (loadCityWeatherList==null || loadCityWeatherList.getStatus() == AsyncTask.Status.FINISHED);
	}
	
	//returns CityWeather list with no data for weather for all cities in db
	public ArrayList<CityWeather> getEmptyCityWeatherList(){
		ArrayList<City> cityList=db.getAllCities();
		ArrayList<CityWeather> cityWeatherList=new ArrayList<CityWeather>();
		for(City city:cityList){
			CityWeather cityWeather=new CityWeather();
			cityWeather.setCity(city);
			cityWeatherList.add(cityWeather);
			Log.d("city in cityweather",cityWeather.getCity().getName());
		}
		return cityWeatherList;
	}
	
	//start async task
	public void loadList(){
		Log.d("Loadlist", "Loadlist");
		
		Button header=(Button)findViewById(R.id.headerCityList);

		if(db.getCityCount()==0){
			header.setText(getResources().getString(R.string.header_no_city));
			cityWeatherList.clear();
			adapter.notifyDataSetChanged();
		}
		//check internet available or not
		else if(!NetworkUtil.isInternetAvailable(AddCityActivity.this)){
			header.setText(getResources().getString(R.string.no_internet));
			Toast.makeText(getBaseContext(),R.string.no_internet,Toast.LENGTH_LONG).show();
			cityWeatherList.clear();
			cityWeatherList.addAll(getEmptyCityWeatherList());
			adapter.notifyDataSetChanged();
		}
		else{
			header.setText(getResources().getString(R.string.header_current_city_weather));
			if(isLoadCityWeatherListFinished()){
				loadCityWeatherList=new LoadCityWeatherList();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					loadCityWeatherList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,getEmptyCityWeatherList());
				else
					loadCityWeatherList.execute(getEmptyCityWeatherList());
			}
		}

	}
	
	//hide virual keyboard on click search
	public void hideVirualKeyBoard(){
		
		InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
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
	
	//search for GPS city
	public void findCurrentLocation(View view){

		Log.d("findcurrent location","called");
		progressDialog = new CustomProgressDialog(this,CustomProgressDialog.SEARCH_LOCATION);
		progressDialog.setMessage(getResources().getString(R.string.searching));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setIndeterminate(false);
		progressDialog.setCancelable(false);

		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			progressDialog.show();
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1000, locationListener);
		}
		else{
			Toast.makeText(getBaseContext(),getResources().getString(R.string.gps_off),Toast.LENGTH_LONG).show();
			showGpsDisabledAlert();
		}	
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

	//Listener for GPS location	
	private class MyCityLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location arg0) {
			if(arg0!=null){
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(),"Latitude: " + arg0.getLatitude()
						+ " Longitude: " + arg0.getLongitude(),Toast.LENGTH_LONG).show();
				String cityName=null;
				Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

				List<Address> addresses;  
				try {  
					addresses = gcd.getFromLocation(arg0.getLatitude(), arg0.getLongitude(), 1);  
					if (addresses.size() > 0){
						cityName=addresses.get(0).getLocality(); 
						if(cityName!=null){
							Toast.makeText(getBaseContext(), cityName, Toast.LENGTH_LONG).show();
							addCity(cityName,"");
							locationManager.removeUpdates(locationListener);
						}
					}
				} catch(IllegalArgumentException e){
					e.printStackTrace();
				}
				catch (IOException e) {   		      
					e.printStackTrace();
				}
			}
		}
		@Override
		public void onProviderDisabled(String arg0) {}
		@Override
		public void onProviderEnabled(String arg0) {}
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
	}
	
	//Async task for current city weather
	private class LoadCityWeatherList extends AsyncTask<ArrayList<CityWeather>,Void,List<CityWeather>>{
		
	
		CustomProgressDialog progressDialog=null;
		@Override
		protected void onPreExecute() {

			progressDialog = new CustomProgressDialog(AddCityActivity.this,CustomProgressDialog.ASYNC_TASK_LOAD_CURRENT_WEATHER);
			progressDialog.setMessage(getResources().getString(R.string.loading));
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);

			progressDialog.show();
			
		}

		@Override
		protected List<CityWeather> doInBackground(ArrayList<CityWeather> ... cityWeatherList) {

			for(CityWeather cityWeather:cityWeatherList[0]){
				if(!isCancelled()){
					String name=cityWeather.getCity().getName();
					Log.d(cityWeather.getCity().getName(),""+cityWeather.getCity().getId());
					String jsonData=( (new JsonHttpClient()).getCityWeatherData(name));
					//Log.d("jsondata",jsonData);
					if(jsonData!=null){
						try{
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
			return cityWeatherList[0];
		}

		@Override
		protected void onPostExecute(List<CityWeather> result) {
			Log.d("async task load list", "on postexecute");
			
			progressDialog.dismiss();
			
			progressDialog=null;
			loadCityWeatherList=null;
			
			cityWeatherList.clear();
			cityWeatherList.addAll(result);
			adapter.notifyDataSetChanged();
		}
		@Override
		protected void onCancelled() {
			Log.d("async task load list", "on cancelled");
			
			progressDialog.dismiss();
			progressDialog=null;
			//Toast.makeText(AddCityActivity.this,"onCancelled",Toast.LENGTH_LONG).show();
			loadCityWeatherList=null;
			cityWeatherList.clear();
			cityWeatherList.addAll(getEmptyCityWeatherList());
			adapter.notifyDataSetChanged();
		}
		
	}

}
