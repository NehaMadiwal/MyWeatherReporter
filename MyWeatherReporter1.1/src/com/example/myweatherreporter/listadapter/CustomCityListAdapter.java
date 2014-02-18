package com.example.myweatherreporter.listadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myweatherreporter.R;
import com.example.myweatherreporter.beans.CityWeather;
import com.example.myweatherreporter.util.StringUtils;

public class CustomCityListAdapter extends ArrayAdapter<CityWeather>{
	
	Context context;
	int resourceId;
	
	private static final String TAG="CustomCityListAdapter";
	
	public CustomCityListAdapter(Context context, int resourceId, ArrayList<CityWeather> cityWeatherItem){
		super(context, resourceId, cityWeatherItem);
		this.context = context;
		this.resourceId=resourceId;
	}
	
	
	private class ViewHolder {
		
		TextView tvCityName;
		TextView tvCountry;
		TextView tvDesc;
		TextView tvTemp;
		TextView tvMaxTemp;
		TextView tvMinTemp;

		ImageView ivImageWeather;

		TextView tvDataNotFound;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d(TAG,"getView()");
		
		ViewHolder holder = null;
		CityWeather cityWeather = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = mInflater.inflate(resourceId, null);
			holder = new ViewHolder();

			holder.tvCityName = (TextView) convertView.findViewById(R.id.tvCityName);
			holder.tvCountry = (TextView) convertView.findViewById(R.id.tvCountry);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
			holder.tvTemp = (TextView) convertView.findViewById(R.id.tvTemperature);
			holder.tvMaxTemp = (TextView) convertView.findViewById(R.id.tvMaxTemp);
			holder.tvMinTemp = (TextView) convertView.findViewById(R.id.tvMinTemp);
			holder.ivImageWeather = (ImageView) convertView.findViewById(R.id.list_image);

			holder.tvDataNotFound= (TextView) convertView.findViewById(R.id.tvNoData);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvCityName.setText(cityWeather.getCity().getCityName());
		

		Log.d(TAG,"data available " +cityWeather.isDataAvailable());

		if(cityWeather.isDataAvailable()){

			holder.tvDataNotFound.setVisibility(View.GONE);
			holder.tvCountry.setText(cityWeather.getCity().getCountry());
			holder.tvDesc.setText(cityWeather.getWeatherDescription());
			holder.tvTemp.setText(""+Math.round((cityWeather.getTemp() - 273.15)) +"\u00B0" +"C");
			holder.tvMaxTemp.setText(""+Math.round((cityWeather.getTempMax() - 273.15)) +"\u00B0" +"C/");
			holder.tvMinTemp.setText(""+Math.round((cityWeather.getTempMin() - 273.15)) +"\u00B0" +"C");
			holder.ivImageWeather.setImageResource(context.getResources().getIdentifier(StringUtils.getIconString(cityWeather.getIcon()), "drawable", context.getPackageName()));
		}
		
		else{
			holder.tvDataNotFound.setVisibility(View.VISIBLE);
			holder.tvCountry.setText("");
			holder.tvDesc.setText("");
			holder.tvTemp.setText("");
			holder.tvMaxTemp.setText("");
			holder.tvMinTemp.setText("");
			holder.ivImageWeather.setImageResource(0);
		}
		return convertView;
		
		
	}

}
