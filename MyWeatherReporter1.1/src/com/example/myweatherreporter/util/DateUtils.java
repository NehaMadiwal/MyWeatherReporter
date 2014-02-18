package com.example.myweatherreporter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	
	private static final DateFormat dateFormat=new SimpleDateFormat("dd MMM,yy");
	private static final DateFormat dateFormatWeekDay=new SimpleDateFormat("EEE");
	private static final DateFormat utcFormat=new SimpleDateFormat("dd MMM,yy/HH:mm:ss");
	
	//get today's date
	public static String getToday(){
		Date today = new Date();
		Calendar tempcal = new GregorianCalendar();
		tempcal.setTime(today);

		return dateFormat.format(tempcal.getTime());

	}

	//get last Nth day's date, day and unix time 
	public static String[] getLastNthDay(int day){
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
}
