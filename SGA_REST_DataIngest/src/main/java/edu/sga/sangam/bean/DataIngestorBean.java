package edu.sga.sangam.bean;

public class DataIngestorBean {
	private int year;
	private int month;
	private int day;
	private String nexrad;
	
	
	public DataIngestorBean(int year, int month, int day, String nexrad) {
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.nexrad = nexrad;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getNexrad() {
		return nexrad;
	}
	public void setNexrad(String nexrad) {
		this.nexrad = nexrad;
	}
	

}
