package edu.sga.sangam.bean;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class StormDetectionResponseBean {
	
	private File kmlfile;
	public StormDetectionResponseBean(File kmlfile) {
		super();
		this.kmlfile = kmlfile;
	}
	
	public File getKmlfile() {
		return kmlfile;
	}

	public void setKmlfile(File kmlfile) {
		this.kmlfile = kmlfile;
	}
	
	public StormDetectionResponseBean()
	{
		
	}

	
}
