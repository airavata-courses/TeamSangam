package edu.sga.sangam.bean;

public class DataIngestorBean {
	private String year;
	private String month;
	private String day;
	private String nexrad;
	
	
	public DataIngestorBean(String year, String month, String day, String nexrad) {
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.nexrad = nexrad;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getNexrad() {
		return nexrad;
	}
	public void setNexrad(String nexrad) {
		this.nexrad = nexrad;
	}
	

}
