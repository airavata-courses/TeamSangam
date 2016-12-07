package edu.sga.sangam.resources;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StormClusterBean {
	String key;
	String stormcluster;
	String sctime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getStormcluster() {
		return stormcluster;
	}
	public void setStormcluster(String stormcluster) {
		this.stormcluster = stormcluster;
	}
	public String getSctime() {
		return sctime;
	}
	public void setSctime(String sctime) {
		this.sctime = sctime;
	}
	
	

}
