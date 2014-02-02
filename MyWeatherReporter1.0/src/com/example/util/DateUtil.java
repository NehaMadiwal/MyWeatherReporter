package com.example.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.util.Log;

public class DateUtil {
	private static final DateFormat dateFormat=new SimpleDateFormat("dd MMM,yy");
	private static final DateFormat dateFormatWeekDay=new SimpleDateFormat("EEE");
	private static final DateFormat utcFormat=new SimpleDateFormat("dd MMM,yy/HH:mm:ss");
	private List <String> dateList;
	
	public String getLast30DayDate(){
		Date today = new Date();
		int days = 0;
		Calendar tempcal = new GregorianCalendar();
		tempcal.setTime(today);

		days = 30;
		tempcal.add(Calendar.DAY_OF_MONTH, -days);
		String startDate = dateFormat.format(tempcal.getTime());

		return startDate;
	}
	
	public static String getToday(){
		Date today = new Date();
		Calendar tempcal = new GregorianCalendar();
		tempcal.setTime(today);
		
		return dateFormat.format(tempcal.getTime());
		
	}
	
	public String[] getLastNthDay(int day){
		Date today = new Date();
		Calendar tempcal = new GregorianCalendar();
		tempcal.setTime(today);
		tempcal.add(Calendar.DAY_OF_MONTH, -day);
		
		Date date= null;
		long unixTime=0l;
		try{
		date = (Date)utcFormat.parse(dateFormat.format(tempcal.getTime())+"/12:00:00");
		//Log.d("timezone",utcFormat.getTimeZone().toString());
		unixTime=date.getTime()/1000;	
	
		}
		catch(ParseException e){
			e.printStackTrace();
		}
		
		return new String[]{dateFormat.format(tempcal.getTime()),dateFormatWeekDay.format(tempcal.getTime()),String.valueOf(unixTime)};
	}
	
	public List<String> getDateList(){
		Date today = new Date();
		Calendar tempcal = new GregorianCalendar();
		tempcal.setTime(today);
		
		dateList= new ArrayList<String>();
		
		/*for(int days = 1;days>=5;days++){
			tempcal.add(Calendar.DAY_OF_MONTH, -1);
			Log.d("days",dateFormat.format(tempcal.getTime()));
			dateList.add("" +days + " "+dateFormat.format(tempcal.getTime()));
		}*/
		
		for(int i=30;i>0;i--){
			dateList.add(" "+i);
		}
		return dateList;
		
	}

}
