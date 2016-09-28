package edu.sga.sangam.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataIngestorStatusBean {
	
	private boolean fileExists;
	private String url;
	
	public DataIngestorStatusBean()
	{
		
	}
	public DataIngestorStatusBean(boolean fileExists, String url) {
		
		this.fileExists = fileExists;
		this.url = url;
	}
	public boolean isFileExists() {
		return fileExists;
	}
	public void setFileExists(boolean fileExists) {
		this.fileExists = fileExists;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
