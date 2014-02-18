package com.example.myweatherreporter.activity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Provider;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myweatherreporter.R;
import com.example.myweatherreporter.beans.City;
import com.example.myweatherreporter.beans.CityHistoryOneDay;
import com.example.myweatherreporter.beans.CityWeather;
import com.example.myweatherreporter.jsonhelper.JsonCityWeatherParser;
import com.example.myweatherreporter.jsonhelper.JsonHttpClient;
import com.example.myweatherreporter.listadapter.CustomCityHistoryListAdapter;
import com.example.myweatherreporter.util.DateUtils;
import com.example.myweatherreporter.util.NetworkUtils;
import com.example.myweatherreporter.util.StringUtils;
import com.example.myweatherreporter.widget.CustomProgressDialog;

public class CityWeatherHistoryActivity extends CityWeatherHistoryBaseActivity {
	
	private static final String TAG="CityWeatherHistoryActivity";
	
	private String cityName=null;
	private String cityCountry=null;
	
	@Inject
	City city;
	
	@Inject Provider<CityWeather> cityWeatherProvider;
	
	private TextView tvCityName;
	private TextView tvCountry;
	private TextView tvCurrentDate;
	private ImageView weatherIcon;
	private TextView tvDescCurrent;
	private TextView tvTemperatureCurrent;
	private TextView tvMinTempCurrent;
	private TextView tvMaxTempCurrent;

	private TextView tvNoCurrentData;

	private TextView tvSourceLink;
	
	private Button buttonHeader;

	private ListView listHistory;
	
	@Inject Provider<CityHistoryOneDay> CityHistoryOneDayProvider;
	@Inject ArrayList<CityHistoryOneDay> dateList;
	
	@Inject	CustomCityHistoryListAdapter adapter;
	

	private LoadCurrentWeather loadCurrentWeather=null;
	private LoadHistoryList loadHistoryList=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate");
		
		setContentView(R.layout.activity_city_weather_history);
		setupActionBar();
		
		tvCityName= (TextView)findViewById(R.id.tvCurrentCityName);
		tvCountry= (TextView)findViewById(R.id.tvCurrentCountry);
		tvNoCurrentData=(TextView)findViewById(R.id.tvNoCurrentData);
		tvSourceLink=(TextView)findViewById(R.id.tvSource);
		tvCurrentDate=(TextView)findViewById(R.id.tvCurrentDate);
		
		weatherIcon=(ImageView)findViewById(R.id.current_image);
		tvDescCurrent=(TextView)findViewById(R.id.tvDescCurrent);
		tvTemperatureCurrent=(TextView)findViewById(R.id.tvTemperatureCurrent);
		tvMinTempCurrent=(TextView)findViewById(R.id.tvMinTempCurrent);
		tvMaxTempCurrent=(TextView)findViewById(R.id.tvMaxTempCurrent);
		
		buttonHeader =(Button)findViewById(R.id.btnHistoryListHeader);
		
		cityName=getIntent().getStringExtra("cityName");
		cityCountry=getIntent().getStringExtra("cityCountry");
		
		tvCityName.setText(cityName);
		tvCountry.setText(cityCountry);
		
		listHistory=(ListView)findViewById(R.id.listHistory);
		
		listHistory.setAdapter(adapter);
		
		city.setCityName(cityName);
		city.setCountry(cityCountry);
		
		
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG,"onStart");
	}


	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		tvCurrentDate.setText(DateUtils.getToday());
		loadData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
		cancelAsyncTasks();
	}


	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG,"onStop");
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy");
	}


	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.city_weather_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh_History:
			loadData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	//check whether LoadCurrentWeather task is running or not
	public boolean isLoadCurrentWeatherFinished(){
		return (loadCurrentWeather==null || loadCurrentWeather.getStatus() == AsyncTask.Status.FINISHED);
	}


	//check whether LoadHistoryWhether task is running or not
	public boolean isLoadHistoryListFinished(){
		return (loadHistoryList==null || loadHistoryList.getStatus() == AsyncTask.Status.FINISHED);
	}


	//lad current weather and weather history data
	public void loadData(){

		if(!NetworkUtils.isInternetAvailable(CityWeatherHistoryActivity.this)){
			buttonHeader.setText(getResources().getString(R.string.no_internet));
			Toast.makeText(getBaseContext(),getResources().getString(R.string.no_internet),Toast.LENGTH_LONG).show();
		}

		else{
			buttonHeader.setText(getResources().getString(R.string.header_history));
			if(isLoadCurrentWeatherFinished())
				loadCurrentWeatherData();
			if(isLoadHistoryListFinished())
				loadHistory();
		}
	}

	
	//start LoadCurrentWeather task
	public void loadCurrentWeatherData(){
		loadCurrentWeather=new LoadCurrentWeather();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			loadCurrentWeather.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,city);
		else
			loadCurrentWeather.execute(city);
	}


	//Start LoadHistoryList task
	public void loadHistory(){
		loadHistoryList=new LoadHistoryList();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			loadHistoryList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		else
			loadHistoryList.execute();
	}
	
	//cancel async tasks
	public void cancelAsyncTasks(){
		if(!isLoadCurrentWeatherFinished()){
			Log.d("CityWeatherHistoryActivity","current weather task not finished");
			loadCurrentWeather.cancel(true);
		}
		if(!isLoadHistoryListFinished()){
			Log.d("CityWeatherHistoryActivity","history task not finished");
			loadHistoryList.cancel(true);
		}
	}

	//Async task for current weather
	private class LoadCurrentWeather extends AsyncTask<City,Void,CityWeather>{
		@Override
		protected CityWeather doInBackground(City... city) {
			String name=city[0].getCityName();
			CityWeather cityWeather=cityWeatherProvider.get();
			cityWeather.setCity(city[0]);
			if(!isCancelled()){
				String jsonData=( (new JsonHttpClient()).getCityWeatherData(name));
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
			return cityWeather;
		}


		@Override
		protected void onPostExecute(CityWeather result) {

			//Log.d("LoadCurrentWeather","onPostExecute");
			if(result.isDataAvailable())
			{
				tvNoCurrentData.setVisibility(View.GONE);
				tvSourceLink.setText(Html.fromHtml(StringUtils.getLinkString(result.getJsonCityId())));
				tvSourceLink.setMovementMethod(LinkMovementMethod.getInstance());

				weatherIcon.setImageResource(CityWeatherHistoryActivity.this.getResources().getIdentifier(StringUtils.getIconString(result.getIcon()), "drawable", CityWeatherHistoryActivity.this.getPackageName()));
				tvDescCurrent.setText(result.getWeatherDescription());
				tvTemperatureCurrent.setText(Math.round((result.getTemp() - 273.15)) +"\u00B0" +"C");
				tvMaxTempCurrent.setText(Math.round(result.getTempMax()-273.15)+"\u00B0" +"C/");
				tvMinTempCurrent.setText(Math.round(result.getTempMin()-273.15)+"\u00B0" +"C");
			}
			else{

				tvNoCurrentData.setVisibility(View.VISIBLE);
				//Log.d("on post execute", "clickable false");
				tvSourceLink.setText(CityWeatherHistoryActivity.this.getResources().getString(R.string.source));
				tvSourceLink.setMovementMethod(null);
				weatherIcon.setImageResource(0);
				tvDescCurrent.setText("");
				tvTemperatureCurrent.setText("");
				tvMaxTempCurrent.setText("");
				tvMinTempCurrent.setText("");
			}
			loadCurrentWeather=null;
		}
		
	}
	
	
	private class LoadHistoryList extends AsyncTask<Void,Void,Void>{

		CustomProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			progressDialog = new CustomProgressDialog(CityWeatherHistoryActivity.this);
			progressDialog.setDialogType(CustomProgressDialog.ASYNC_TASK_LOAD_HISTORY);
			progressDialog.setMessage(getResources().getString(R.string.loading));

			progressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {

			dateList.clear();

			for(int i=1;i<=30;i++)
			{
				if(!isCancelled()){
					String dateDay[]=DateUtils.getLastNthDay(i);
					CityHistoryOneDay cityHistoryOneDay=CityHistoryOneDayProvider.get();
					cityHistoryOneDay.setDate(dateDay[0]);
					cityHistoryOneDay.setDay(dateDay[1]);

					String jsonData=( (new JsonHttpClient()).getCityWeatherHistoryData(cityName,dateDay[2]));

					//Log.d("history data:", jsonData);
					if(jsonData!=null){
						try{
							cityHistoryOneDay=JsonCityWeatherParser.getCityWeatherHistory(jsonData,cityHistoryOneDay);
							cityHistoryOneDay.setDataAvailable(true);
						}catch(JSONException e){
							cityHistoryOneDay.setDataAvailable(false);
							Log.d("json exception", "json exception");
							e.printStackTrace();
						}
					}
					else
						cityHistoryOneDay.setDataAvailable(false);
					dateList.add(cityHistoryOneDay);
				}
				else 
					break;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();

			progressDialog=null;
			loadHistoryList=null;

			adapter.notifyDataSetChanged();
		}
		
		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			Log.d("async history task","on cancelled");

			progressDialog=null;
			loadHistoryList=null;

			dateList.clear();
			adapter.notifyDataSetChanged();

		}

	}

}
