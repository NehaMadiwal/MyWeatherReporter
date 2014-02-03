package com.example.myweatherreporterbeans;
public class City {

	int id;
	String name;
	String country;
	
	public City(){}
	public City(String name,String country){
		this.name=name;
		this.country=country;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String toString(){
		return id+":"+name+":"+country;
	}
}
