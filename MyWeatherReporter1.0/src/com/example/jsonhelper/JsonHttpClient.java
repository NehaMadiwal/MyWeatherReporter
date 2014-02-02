package com.example.jsonhelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

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
	private static String IMAGE_URL="http://openweathermap.org/img/w/";
	private static String HISTORY_URL="http://api.openweathermap.org/data/2.5/history/city?q=";
	private static String START="&start=";
	private static String CNT="&cnt=2";
	
	
	
	private static String PNG=".png";
	
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
	
	public byte[] getImage(String icon){
		HttpURLConnection con = null ;
		InputStream is = null;
		Log.d("image url",IMAGE_URL + icon+PNG);

		try {
			con = (HttpURLConnection) ( new URL(IMAGE_URL + icon+PNG)).openConnection();
			con.setRequestMethod("GET");
			//con.setDoInput(true);
			//con.setDoOutput(true);
			con.connect();

			is = con.getInputStream();
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ( is.read(buffer) != -1)
				baos.write(buffer);

			return baos.toByteArray();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		finally {
			try { is.close(); } catch(Throwable t) {}
			try { con.disconnect(); } catch(Throwable t) {}
		}

		return null;
	}
}
