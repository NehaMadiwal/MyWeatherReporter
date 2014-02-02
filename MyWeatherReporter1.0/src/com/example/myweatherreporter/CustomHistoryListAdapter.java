package com.example.myweatherreporter;

import java.util.List;

import com.example.myweatherreporter1.R;
import com.example.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomHistoryListAdapter extends ArrayAdapter<CityHistoryOneDay>{
	

	Context context;
	int resourceId;
	
	public CustomHistoryListAdapter(Context context, int resourceId,
            List<CityHistoryOneDay> historyItems){
    	super(context, resourceId, historyItems);
        this.context = context;
        this.resourceId=resourceId;
    }
	
	private class ViewHolder {
		TextView txtDate;
		TextView txtDay;
		TextView txtDesc;
		//TextView txtTemp;
		TextView txtMaxTemp;
		TextView txtMinTemp;
		
		ImageView imageWeather;

		TextView txtHistoryNotFound;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		CityHistoryOneDay historyOneDay =getItem(position);
		LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
            convertView = mInflater.inflate(resourceId, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.dateHistory);
            holder.txtDay = (TextView) convertView.findViewById(R.id.dayHistory);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.descHistory);
            //holder.txtTemp = (TextView) convertView.findViewById(R.id.temperatureHistory);
            holder.txtMaxTemp = (TextView) convertView.findViewById(R.id.maxTempHistory);
            holder.txtMinTemp = (TextView) convertView.findViewById(R.id.minTempHistory);
            holder.imageWeather=(ImageView) convertView.findViewById(R.id.list_history_image);
            
            holder.txtHistoryNotFound= (TextView) convertView.findViewById(R.id.no_data_history);
            
            convertView.setTag(holder);
        }else
        	holder = (ViewHolder) convertView.getTag();
		
		Log.d("data available",""+historyOneDay.isDataAvailable());
		
		holder.txtDate.setText(historyOneDay.getDate());
		holder.txtDay.setText(historyOneDay.getDay());
		
		if(historyOneDay.isDataAvailable()){
			
			holder.txtHistoryNotFound.setVisibility(View.GONE);
			
			holder.txtDesc.setText(historyOneDay.getWeatherDescription());
			//holder.txtTemp.setText(""+Math.round((historyOneDay.getTemp() - 273.15)) +"\u00B0" +"C");
			holder.txtMaxTemp.setText(""+Math.round(historyOneDay.getTempMax()-273.15)+"\u00B0" +"C/");
			holder.txtMinTemp.setText(""+Math.round(historyOneDay.getTempMin()-273.15)+"\u00B0" +"C");

			holder.imageWeather.setImageResource(context.getResources().getIdentifier(StringUtils.getIconString(historyOneDay.getIcon()), "drawable", context.getPackageName()));
		}
		else{
			holder.txtHistoryNotFound.setVisibility(View.VISIBLE);

			holder.txtDesc.setText(historyOneDay.getWeatherDescription());
			//holder.txtTemp.setText("");
			holder.txtMaxTemp.setText("");
			holder.txtMinTemp.setText("");

			holder.imageWeather.setImageResource(0);
		}
		return convertView;
	}

}
