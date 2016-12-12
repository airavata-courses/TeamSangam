package edu.sga.sangam.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultBean {
	
	public String keyid;
	public String value;
	public String jobid;
	public String resulttime;
	public String getKeyid() {
		return keyid;
	}
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getResultime() {
		return resulttime;
	}
	public void setResultime(String resultime) {
		this.resulttime = resultime;
	}
	public void setJobid(String jobid)
	{
		this.jobid = jobid;
	}
	public String getJobid()
	{
		return jobid;
	}

}
