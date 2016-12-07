package edu.sga.sangam.resources;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RunForecastBean {
	String key;
	String runforecast;
	String rftime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getRunforecast() {
		return runforecast;
	}
	public void setRunforecast(String runforecast) {
		this.runforecast = runforecast;
	}
	public String getRftime() {
		return rftime;
	}
	public void setRftime(String rftime) {
		this.rftime = rftime;
	}
	
	
	

}

