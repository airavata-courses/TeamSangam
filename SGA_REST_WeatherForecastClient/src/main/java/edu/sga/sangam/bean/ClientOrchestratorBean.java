package edu.sga.sangam.bean;

public class ClientOrchestratorBean {
	String year;
	String mm;
	String day;
	String nexrad;
	public ClientOrchestratorBean(String year, String mm, String day, String nexrad, String fileName) {
		super();
		this.year = year;
		this.mm = mm;
		this.day = day;
		this.nexrad = nexrad;
		this.fileName = fileName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMm() {
		return mm;
	}
	public void setMm(String mm) {
		this.mm = mm;
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
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	String fileName;
}
