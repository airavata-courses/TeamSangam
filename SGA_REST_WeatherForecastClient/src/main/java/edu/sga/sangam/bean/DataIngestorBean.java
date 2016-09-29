package edu.sga.sangam.bean;

public class DataIngestorBean {
	
	private int status;
	private boolean fileexists;
	private String url;
	
	
	public DataIngestorBean(int status, boolean fileexists, String url) {
		super();
		this.status = status;
		this.fileexists = fileexists;
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isFileexists() {
		return fileexists;
	}
	public void setFileexists(boolean fileexists) {
		this.fileexists = fileexists;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
