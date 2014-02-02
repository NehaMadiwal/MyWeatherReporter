package com.example.myweatherreporterdb;

import java.util.ArrayList;

import com.example.util.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="CityDB";
	
	
	private static final String TABLE_CITY="city";
	
	private static final String KEY_ID="_id";
	private static final String KEY_NAME="name";
	private static final String KEY_COUNTRY="country";

	public DatabaseHandler(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CITY);
		String CREATE_CITY_TABLE = "CREATE TABLE "+ TABLE_CITY + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT,"
				+ KEY_COUNTRY + " TEXT, " +" UNIQUE("+ KEY_NAME +","+ KEY_COUNTRY +") ON CONFLICT ABORT"+")";
		db.execSQL(CREATE_CITY_TABLE);
		Log.d("onCreate():", "DB created"+ CREATE_CITY_TABLE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("onUpgrade():", "DB upgraded");
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CITY);
		this.onCreate(db);
		
	}
	
	public void deleteDatabase()
	{
		Log.d("deleteDatabase():", "DB deleted");
		this.deleteDatabase();
		
		//SQLiteDatabase db=this.getWritableDatabase();
		//db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CITY);
	}
	
	public boolean addCity(City city){
		boolean duplicateCity=false;
		
		SQLiteDatabase db=this.getWritableDatabase();
		ContentValues values =new ContentValues();
		values.put(KEY_NAME, city.getName());
		values.put(KEY_COUNTRY, city.getCountry());
		try{
		//db.insertOrThrow(table, nullColumnHack, values)
		db.insertOrThrow(TABLE_CITY, null, values);
		}
		catch(SQLiteConstraintException e){
			duplicateCity=true;
			Log.d("duplicate record"+e.getMessage(), "city="+city.getName()+" country="+city.getCountry());			
		}
		if(!duplicateCity)
			Log.d("add city",city.toString());
		else
			Log.d("add city", "city not added");
		db.close();
		
		return duplicateCity;
	}
	
	public City getCity(int id){
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_CITY, new String[] { KEY_ID,
	            KEY_NAME, KEY_COUNTRY }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if(cursor!=null)
			cursor.moveToFirst();
		
		City city=new City(cursor.getString(1),cursor.getString(2));
		city.setId(Integer.parseInt(cursor.getString(0)));
		
		Log.d("getCity(" + id +")", city.toString());
		return city;
	}
	
	public ArrayList<City> getAllCities(){
		ArrayList<City> cityList=new ArrayList<City>();
		
		String query="SELECT * FROM "+ TABLE_CITY;
		
		SQLiteDatabase db= this.getReadableDatabase();
		
		Cursor cursor=db.rawQuery(query, null);
		
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(Integer.parseInt(cursor.getString(0)));
				city.setName(StringUtils.toTitleCase(cursor.getString(1)));
				city.setCountry(cursor.getString(2));
				
				cityList.add(city);
			}while(cursor.moveToNext());
		}
		
		Log.d("getAllCities()", cityList.toString());
		return cityList;
	}
	
	public Cursor getAllCitiesCursor(){
		String query="SELECT * FROM "+ TABLE_CITY;

		SQLiteDatabase db= this.getReadableDatabase();

		Cursor cursor=db.rawQuery(query, null);

		return cursor;
	}
	
	public int getCityCount(){
		String countQuery="SELECT * FROM "+ TABLE_CITY;
		SQLiteDatabase db=this.getReadableDatabase();
		
		Cursor cursor=db.rawQuery(countQuery, null);
		int count=cursor.getCount();
		cursor.close();
		
		Log.d("getAllCities()", ""+count);
		return count;
	}
	
	public int updateCity(City city){
		SQLiteDatabase db= this.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put(KEY_NAME, city.getName());
		values.put(KEY_COUNTRY, city.getCountry());
		
		int i=db.update(TABLE_CITY, values, KEY_ID+ " =?", new String[]{String.valueOf(city.getId())});
		db.close();
		return i;
	}
	
	public void deleteCity(City city){
		Log.d("deleteCity() ",city.toString());
		
		SQLiteDatabase db= this.getWritableDatabase();
		db.delete(TABLE_CITY, KEY_ID + " =?", new String[]{String.valueOf(city.getId())});
		db.close();
		
		
	}

}
