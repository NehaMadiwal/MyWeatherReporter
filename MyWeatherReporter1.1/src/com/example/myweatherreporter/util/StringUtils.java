package com.example.myweatherreporter.util;

public class StringUtils {
	
	private static final String HTML_START="<html><a href=\"http://m.openWeatherMap.org/city/";
	private static final String HTML_END="\">openweathermap.org</a></html>";

	//convert string to title case
	public static String toTitleCase(String str){
		String[] arr = str.toLowerCase().split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1)).append(" ");
		}          
		return sb.toString().trim();
	}


	public static String getIconString(String iconString){
		if(iconString.equals("01d"))
			return "d01";
		else if(iconString.equals("01n"))
			return "n01";
		else if(iconString.equals("02d"))
			return "d02";
		else if(iconString.equals("02n"))
			return "n02";
		else if(iconString.equals("03d"))
			return "d03";
		else if(iconString.equals("03n"))
			return "n03";
		else if(iconString.equals("04d"))
			return "d04";
		else if(iconString.equals("04n"))
			return "n04";
		else if(iconString.equals("09d"))
			return "d09";
		else if(iconString.equals("09n"))
			return "n09";
		else if(iconString.equals("10d"))
			return "d10";
		else if(iconString.equals("10n"))
			return "n10";
		else if(iconString.equals("11d"))
			return "d11";
		else if(iconString.equals("11n"))
			return "n11";
		else if(iconString.equals("13d"))
			return "d13";
		else if(iconString.equals("13n"))
			return "n13";
		else if(iconString.equals("50d"))
			return "d50";
		else if(iconString.equals("50n"))
			return "n50";
		else
			return null;
	}
	
	//openweathermap link for city with id "cityId"
	public static String getLinkString(int cityId){
		return HTML_START + String.valueOf(cityId) + HTML_END ;
	}
		
}
