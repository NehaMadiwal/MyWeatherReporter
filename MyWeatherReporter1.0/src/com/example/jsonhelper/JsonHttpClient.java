package com.example.jsonhelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.example.util.StringUtils;

public class JsonHttpClient {

	private static String BASE_URL="http://api.openweathermap.org/data/2.5/weather?q=";
	private static String HISTORY_URL="http://api.openweathermap.org/data/2.5/history/city?q=";
	private static String START="&start=";
	private static String CNT="&cnt=2";


	//get current weather for city "cityName"
	public String getCityWeatherData(String cityName){
		cityName=StringUtils.replaceInString(cityName, " ", "%20");
		Log.d("CityName:",cityName);
		String jsonString=null;
		try {
			Log.d("url",BASE_URL+cityName);

			HttpUriRequest httpGet = new HttpGet(BASE_URL+cityName);
			HttpParams httpParameters = new BasicHttpParams();

			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			//Log.d("File Size", httpEntity.getContentLength()+" bytes");
			jsonString=EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonString;

	}

	//get weather history data for "cityName" at time "unixTime"
	public String getCityWeatherHistoryData(String cityName,String unixTime){

		String jsonString=null;
		cityName=StringUtils.replaceInString(cityName, " ", "%20");
		try {
			//Log.d("url", HISTORY_URL+cityName+START+unixTime+CNT);
			HttpUriRequest httpGet = new HttpGet(HISTORY_URL+cityName+START+unixTime+CNT);
			HttpParams httpParameters = new BasicHttpParams();

			int timeoutConnection = 3000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = null;

			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			jsonString=EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonString;

	}

}
