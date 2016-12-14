package edu.sga.sangam.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OrchestratorBean {
	
	String key;
	String userid;
	String sessionid;
	String requestid;
	String year;
	String month;
	String day;
	String nexrad;
	String fileName;
	String orchestrator;
	String time;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
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
	public String getOrchestrator() {
		return orchestrator;
	}
	public void setOrchestrator(String orchestrator) {
		this.orchestrator = orchestrator;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
