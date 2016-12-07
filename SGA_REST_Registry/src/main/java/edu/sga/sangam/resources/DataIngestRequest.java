package edu.sga.sangam.resources;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class DataIngestRequest {
	String key;
	String dataingestor;
	String dttime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDataingestor() {
		return dataingestor;
	}
	public void setDataingestor(String dataingestor) {
		this.dataingestor = dataingestor;
	}
	public String getDttime() {
		return dttime;
	}
	public void setDttime(String dttime) {
		this.dttime = dttime;
	}
	

}
