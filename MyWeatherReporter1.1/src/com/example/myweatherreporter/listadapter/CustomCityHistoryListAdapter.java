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
import com.example.myweatherreporter.beans.CityHistoryOneDay;
import com.example.myweatherreporter.util.StringUtils;

public class CustomCityHistoryListAdapter extends ArrayAdapter<CityHistoryOneDay>{
	
	Context context;
	int resourceId;

	private static final String TAG="CustomCityHistoryListAdapter";

	public CustomCityHistoryListAdapter(Context context, int resourceId,ArrayList<CityHistoryOneDay> historyItems){
		super(context, resourceId, historyItems);
		this.context = context;
		this.resourceId=resourceId;
	}

	private class ViewHolder {
		TextView tvDate;
		TextView tvDay;
		TextView tvDesc;
		TextView tvMaxTemp;
		TextView tvMinTemp;

		ImageView ivImageWeather;

		TextView tvHistoryNotFound;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d(TAG,"getView()");
		
		ViewHolder holder = null;
		
		CityHistoryOneDay historyOneDay =getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(resourceId, null);
			holder = new ViewHolder();
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDateHistory);
			holder.tvDay = (TextView) convertView.findViewById(R.id.tvDayHistory);
			holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDescHistory);
			holder.tvMaxTemp = (TextView) convertView.findViewById(R.id.tvMaxTempHistory);
			holder.tvMinTemp = (TextView) convertView.findViewById(R.id.tvMinTempHistory);
			holder.ivImageWeather=(ImageView) convertView.findViewById(R.id.list_history_image);

			holder.tvHistoryNotFound= (TextView) convertView.findViewById(R.id.tvNoDataHistory);

			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();

		Log.d("data available",""+historyOneDay.isDataAvailable());

		holder.tvDate.setText(historyOneDay.getDate());
		holder.tvDay.setText(historyOneDay.getDay());

		//check if weather history is available
		if(historyOneDay.isDataAvailable()){

			holder.tvHistoryNotFound.setVisibility(View.GONE);

			holder.tvDesc.setText(historyOneDay.getWeatherDescription());
			holder.tvMaxTemp.setText(""+Math.round(historyOneDay.getTempMax()-273.15)+"\u00B0" +"C/");
			holder.tvMinTemp.setText(""+Math.round(historyOneDay.getTempMin()-273.15)+"\u00B0" +"C");

			holder.ivImageWeather.setImageResource(context.getResources().getIdentifier(StringUtils.getIconString(historyOneDay.getIcon()), "drawable", context.getPackageName()));
		}
		else{
			holder.tvHistoryNotFound.setVisibility(View.VISIBLE);

			holder.tvDesc.setText(historyOneDay.getWeatherDescription());
			//holder.txtTemp.setText("");
			holder.tvMaxTemp.setText("");
			holder.tvMinTemp.setText("");

			holder.ivImageWeather.setImageResource(0);
		}
		return convertView;
	}


}
