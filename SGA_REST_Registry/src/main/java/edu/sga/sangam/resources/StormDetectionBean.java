package edu.sga.sangam.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StormDetectionBean {
	String key;
	String stormdetection;
	String sdtime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getStormdetection() {
		return stormdetection;
	}
	public void setStormdetection(String stormdetection) {
		this.stormdetection = stormdetection;
	}
	public String getSdtime() {
		return sdtime;
	}
	public void setSdtime(String sdtime) {
		this.sdtime = sdtime;
	}
	
	
}
