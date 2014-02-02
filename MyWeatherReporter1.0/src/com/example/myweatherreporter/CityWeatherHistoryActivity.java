package com.example.myweatherreporter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
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

import com.example.jsonhelper.JsonCityWeatherParser;
import com.example.jsonhelper.JsonHttpClient;
import com.example.myweatherreporteradapter.CustomHistoryListAdapter;
import com.example.myweatherreporterbeans.City;
import com.example.myweatherreporterbeans.CityHistoryOneDay;
import com.example.myweatherreporterbeans.CityWeather;
import com.example.util.DateUtil;
import com.example.util.NetworkUtil;
import com.example.util.StringUtils;

public class CityWeatherHistoryActivity extends Activity {
	
	private String cityName=null;
	private String cityCountry=null;
	private City city=null;
	
	private TextView txtCityName;
	private TextView txtCountry;
	private TextView txtCurrentDate;
	private ImageView weatherIcon;
	private TextView descCurrent;
	private TextView temperatureCurrent;
	private TextView minTempCurrent;
	private TextView maxTempCurrent;
	
	private TextView txtNoCurrentData;
	
	private TextView txtSourceLink;
	
	private ListView listHistory;
	
	List<CityHistoryOneDay> dateList=null;
	CustomHistoryListAdapter adapter=null;
	
	//Async tasks
	private LoadHistoryList loadHistoryList=null;
	private LoadCurrentWeather loadCurrentWeather=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_weather_history);
		setupActionBar();
		
		
		txtCityName= (TextView)findViewById(R.id.cityName);
		txtCountry= (TextView)findViewById(R.id.countryName);
		
		txtNoCurrentData=(TextView)findViewById(R.id.no_data_current);
		txtSourceLink=(TextView)findViewById(R.id.source);
		
		txtCurrentDate=(TextView)findViewById(R.id.currentDate);
		
		cityName=getIntent().getStringExtra("cityName");
		cityCountry=getIntent().getStringExtra("cityCountry");
		
		txtCityName.setText(cityName);
		txtCountry.setText(cityCountry);
		
		weatherIcon=(ImageView)findViewById(R.id.current_image);
		descCurrent=(TextView)findViewById(R.id.descCurrent);
		temperatureCurrent=(TextView)findViewById(R.id.temperatureCurrent);
		minTempCurrent=(TextView)findViewById(R.id.minTempCurrent);
		maxTempCurrent=(TextView)findViewById(R.id.maxTempCurrent);
		
		listHistory=(ListView)findViewById(R.id.listHistory);
		
		dateList=new ArrayList<CityHistoryOneDay>();
		adapter=new CustomHistoryListAdapter(CityWeatherHistoryActivity.this,R.layout.history_list_item,dateList);
		listHistory.setAdapter(adapter);
		
		city=new City(cityName,cityCountry);
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("CityWeatherHistoryActivity","onResume");
		txtCurrentDate.setText(DateUtil.getToday());
		loadData();
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("CityWeatherHistoryActivity","onPause");
		cancelAsyncTasks();
	}
	
	@Override
	protected void onDestroy() {
		Log.d("CityWeatherHistoryActivity","onDestory");
		cancelAsyncTasks();
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		Log.d("CityWeatherHistoryActivity","onBackPressed");
		super.onBackPressed();
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
		switch(item.getItemId()){
		case R.id.action_refresh_History :
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
	
	//Load current weather and history data
	public void loadData(){
		Button buttonHeader=(Button)findViewById(R.id.header);
		
		if(!NetworkUtil.isInternetAvailable(CityWeatherHistoryActivity.this)){
			buttonHeader.setText(getResources().getString(R.string.no_internet));
			Toast.makeText(getBaseContext(),R.string.no_internet,Toast.LENGTH_LONG).show();
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

	//Async task for current weather
	private class LoadCurrentWeather extends AsyncTask<City,Void,CityWeather>{

		@Override
		protected CityWeather doInBackground(City... city) {
			String name=city[0].getName();
			CityWeather cityWeather=new CityWeather();
			cityWeather.setCity(new City(name,""));
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

			Log.d("LoadCurrentWeather","onPostExecute");
			if(result.isDataAvailable())
			{
				txtNoCurrentData.setVisibility(View.GONE);
				//txtSourceLink.setLinksClickable(true);
				txtSourceLink.setText(Html.fromHtml(StringUtils.getLinkString(result.getJsonCityId())));
				txtSourceLink.setMovementMethod(LinkMovementMethod.getInstance());
				
				weatherIcon.setImageResource(CityWeatherHistoryActivity.this.getResources().getIdentifier(StringUtils.getIconString(result.getIcon()), "drawable", CityWeatherHistoryActivity.this.getPackageName()));
				descCurrent.setText(result.getWeatherDescription());
				temperatureCurrent.setText(Math.round((result.getTemp() - 273.15)) +"\u00B0" +"C");
				maxTempCurrent.setText(Math.round(result.getTempMax()-273.15)+"\u00B0" +"C/");
				minTempCurrent.setText(Math.round(result.getTempMin()-273.15)+"\u00B0" +"C");
			}
			else{
				
				txtNoCurrentData.setVisibility(View.VISIBLE);
				Log.d("on post execute", "clickable false");
				//txtSourceLink.setLinksClickable(false);
				txtSourceLink.setText(CityWeatherHistoryActivity.this.getResources().getString(R.string.source));
				txtSourceLink.setMovementMethod(null);
				weatherIcon.setImageResource(0);
				descCurrent.setText("");
				temperatureCurrent.setText("");
				maxTempCurrent.setText("");
				minTempCurrent.setText("");
			}
			loadCurrentWeather=null;
		}
		@Override
		protected void onCancelled() {
			Log.d("LoadCurrentWeather","onCancelled");
			
			txtNoCurrentData.setVisibility(View.VISIBLE);
			//txtSourceLink.setLinksClickable(false);
			txtSourceLink.setText(CityWeatherHistoryActivity.this.getResources().getString(R.string.source));
			txtSourceLink.setMovementMethod(null);
			
			
			Log.d("on cancelled", "clickable false");
			
			weatherIcon.setImageResource(0);
			descCurrent.setText("");
			temperatureCurrent.setText("");
			maxTempCurrent.setText("");
			minTempCurrent.setText("");
			loadCurrentWeather=null;
		}
	}
	
	//async task for history
	private class LoadHistoryList extends AsyncTask<Void,Void,List<CityHistoryOneDay>>{

		CustomProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			progressDialog = new CustomProgressDialog(CityWeatherHistoryActivity.this,CustomProgressDialog.ASYNC_TASK_LOAD_HISTORY);
			progressDialog.setMessage(getResources().getString(R.string.loading));
			progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
 
            progressDialog.show();
		}
		
		@Override
		protected List<CityHistoryOneDay> doInBackground(Void... arg0) {
			DateUtil util=new DateUtil();
			ArrayList<CityHistoryOneDay> list=new ArrayList<CityHistoryOneDay>();
			for(int i=1;i<=30;i++)
			{
				if(!isCancelled()){
					String dateDay[]=util.getLastNthDay(i);
					CityHistoryOneDay cityHistoryOneDay=new CityHistoryOneDay();
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
					list.add(cityHistoryOneDay);
				}
				else 
					break;
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<CityHistoryOneDay> result) {

			progressDialog.dismiss();

			progressDialog=null;
			loadHistoryList=null;

			dateList.clear();
			dateList.addAll(result);
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
