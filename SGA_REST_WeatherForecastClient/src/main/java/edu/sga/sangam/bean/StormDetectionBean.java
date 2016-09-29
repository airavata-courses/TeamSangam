package edu.sga.sangam.bean;

import java.io.File;

public class StormDetectionBean {
	
	private int status;
	private File file;
	public StormDetectionBean(int status, File file) {
		super();
		this.status = status;
		this.file = file;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	

}
