package edu.sga.sangam.bean;

import java.util.Locale;

public class ResultResponseBean {
	private String jobid;
	private String jsonString;
	
	public ResultResponseBean()
	{
		
	}
	
	
	public ResultResponseBean(String jobid, String jsonString) {
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
		return String.format(Locale.ROOT, "%s#%s",jobid,jsonString);
	}
	
}
