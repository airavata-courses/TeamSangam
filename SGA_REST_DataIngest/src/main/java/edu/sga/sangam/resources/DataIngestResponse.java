package edu.sga.sangam.resources;

import java.util.Locale;

public class DataIngestResponse {
	private String year;
	private String month;
	private String day;
	private String nexrad;
	private String fileName;
	private String userid;
	private String sessionid;
	private String requestid;
	public DataIngestResponse()
	{
		
	}
	
	public DataIngestResponse(String year, String month, String day, String nexrad, String fileName, String userid,
			String sessionid, String requestid) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.nexrad = nexrad;
		this.fileName = fileName;
		this.userid = userid;
		this.sessionid = sessionid;
		this.requestid = requestid;
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
	@Override
	public String toString() {
		/*return "DataIngestorResponse [year=" + year + ", month=" + month + ", day=" + day + ", nexrad=" + nexrad
				+ ", fileName=" + fileName + ", userid=" + userid + ", sessionid=" + sessionid + ", requestid="
				+ requestid + "]";*/
		return String.format(Locale.ROOT, "%s,%s,%s,%s,%s,%s,%s,%s",year,month,day,nexrad,fileName,userid,sessionid,requestid);
		
	}
}

