package edu.sga.sangam.resources;

import java.util.Locale;

public class RunForecastBean {
	
	private String jobid;
	private String jsonString;
	

	public RunForecastBean(String jobid, String jsonString) {
		super();
		this.jobid = jobid;
		this.jsonString = jsonString;
	}
	
	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String toString() {
	String data =	String.format(Locale.ROOT, "%s#%s",jobid,jsonString);
	return data;
	}
	

}
