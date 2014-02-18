package com.example.myweatherreporter.db;

import java.util.ArrayList;

import javax.inject.Inject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myweatherreporter.beans.City;
import com.example.myweatherreporter.util.StringUtils;


public class DatabaseHandler extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="citydb";

	private static final String TABLE_CITY="city";

	private static final String KEY_ID="_id";
	private static final String KEY_NAME="city_name";

	private SQLiteDatabase db;

	private String TAG = "Database Handler";

	@Inject
	public DatabaseHandler(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		Log.d(TAG, "database handler constructor");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CITY);
		String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_CITY + "( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_NAME + " TEXT" +  " UNIQUE )" ;

		Log.d(TAG, CREATE_CITY_TABLE);
		db.execSQL(CREATE_CITY_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CITY);
		this.onCreate(db);
	}

	public void deleteDatabase()
	{
		Log.d("deleteDatabase():", "DB deleted");
		this.deleteDatabase();
	}

	//insert city into db
	public boolean addCity(City city){

		boolean duplicateCity=false;

		db= this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, city.getCityName());
		try{
			db.insertOrThrow(TABLE_CITY, null, values);
		}catch(SQLiteConstraintException e){
			duplicateCity=true;
			Log.d(TAG,e.getMessage()+" city= "+city.getCityName());			
		}finally{
			db.close();
		}

		Log.d(TAG," city= "+city.getCityName());
		return duplicateCity;
	}

	//get city from id
	public City getCity(int id){
		db=this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_CITY, new String[] {KEY_ID, KEY_NAME}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null,null);

		if(cursor !=null)
			cursor.moveToFirst();

		City city=new City(cursor.getString(1));
		city.setId(Integer.parseInt(cursor.getString(0)));

		cursor.close();
		db.close();

		return city;
	}

	//get all cities in the db
	public ArrayList<City> getAllCities(){
		ArrayList<City> cityList=new ArrayList<City>();
		String query="SELECT * FROM " + TABLE_CITY ;

		db=this.getReadableDatabase();

		Cursor cursor= db.rawQuery(query, null);

		if(cursor !=null && cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(Integer.parseInt(cursor.getString(0)));
				city.setCityName(StringUtils.toTitleCase(cursor.getString(1)));
				cityList.add(city);
			}while(cursor.moveToNext());
		}

		cursor.close();
		db.close();
		Log.d(TAG, cityList.toString());

		return cityList;
	}

	//get count of cities in db
	public int getCityCount(){
		String countQuery="SELECT * FROM "+ TABLE_CITY;
		db=this.getReadableDatabase();

		Cursor cursor=db.rawQuery(countQuery, null);
		int count=cursor.getCount();
		cursor.close();
		db.close();

		return count;

	}

	//delete city from db
	public void deleteCity(City city){
		db= this.getWritableDatabase();
		db.delete(TABLE_CITY, KEY_ID + " =?", new String[]{String.valueOf(city.getId())});
		db.close();
	}


}
