package edu.sga.sangam.bean;

public class ClientOrchestratorBean {
	String year;
	String mm;
	String day;
	String nexrad;
	String fileName;
	String userid;
	String sessionid;
	String requestid;
	
	public ClientOrchestratorBean(String year, String mm, String day, String nexrad , String fileName,String userid, String sessionid,
			String requestid) {
		super();
		this.year = year;
		this.mm = mm;
		this.day = day;
		this.nexrad = nexrad;
		this.userid = userid;
		this.sessionid = sessionid;
		this.requestid = requestid;
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getRequestid() {
		return requestid;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	
	
}
