package edu.sga.sangam.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForecastDecisionBean {
	String key;
	String forecast;
	String fctime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getForecast() {
		return forecast;
	}
	public void setForecast(String forecast) {
		this.forecast = forecast;
	}
	public String getFctime() {
		return fctime;
	}
	public void setFctime(String fctime) {
		this.fctime = fctime;
	}
	
}
