package com.example.myweatherreporter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsonhelper.CityWeather;
import com.example.myweatherreporter1.R;
import com.example.util.StringUtils;

public class CityListAdapter extends ArrayAdapter<CityWeather>{
	Context context;
	int resourceId;
	
	public CityListAdapter(Context context, int resourceId,
            List<CityWeather> cityWeatherItem){
    	super(context, resourceId, cityWeatherItem);
        this.context = context;
        this.resourceId=resourceId;
    }
	
	private class ViewHolder {
		TextView txtCityName;
		TextView txtCountry;
		TextView txtDesc;
		TextView txtTemp;
		TextView txtMaxTemp;
		TextView txtMinTemp;
		
		ImageView imageWeather;
		
		TextView txtDataNotFound;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		CityWeather cityWeather =getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            
            holder.txtCityName = (TextView) convertView.findViewById(R.id.name);
            holder.txtCountry = (TextView) convertView.findViewById(R.id.country);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTemp = (TextView) convertView.findViewById(R.id.temperature);
            holder.txtMaxTemp = (TextView) convertView.findViewById(R.id.maxTemp);
            holder.txtMinTemp = (TextView) convertView.findViewById(R.id.minTemp);
            holder.imageWeather = (ImageView) convertView.findViewById(R.id.list_image);
            
            holder.txtDataNotFound= (TextView) convertView.findViewById(R.id.no_data);
            
            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtCityName.setText(cityWeather.getCity().getName());
		
		Log.d("data available",""+cityWeather.isDataAvailable());
		
		if(cityWeather.isDataAvailable()){

			holder.txtDataNotFound.setVisibility(View.GONE);

			//holder.txtCityName.setText(cityWeather.getCity().getName());
			holder.txtCountry.setText(cityWeather.getCity().getCountry());
			holder.txtDesc.setText(cityWeather.getWeatherDescription());
			holder.txtTemp.setText(""+Math.round((cityWeather.getTemp() - 273.15)) +"\u00B0" +"C");
			holder.txtMaxTemp.setText(""+Math.round((cityWeather.getTempMax() - 273.15)) +"\u00B0" +"C/");
			holder.txtMinTemp.setText(""+Math.round((cityWeather.getTempMin() - 273.15)) +"\u00B0" +"C");
			holder.imageWeather.setImageResource(context.getResources().getIdentifier(StringUtils.getIconString(cityWeather.getIcon()), "drawable", context.getPackageName()));
		}
		else{
			holder.txtDataNotFound.setVisibility(View.VISIBLE);
			//holder.txtCityName.setText(cityWeather.getCity().getName());
			holder.txtCountry.setText("");
			holder.txtDesc.setText("");
			holder.txtTemp.setText("");
			holder.txtMaxTemp.setText("");
			holder.txtMinTemp.setText("");
			holder.imageWeather.setImageResource(0);
		}
			
		
		return convertView;
	}
	
	

}
