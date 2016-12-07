package edu.sga.sangam.services;

import edu.sga.sangam.bean.DataIngestorBean;

public class DataIngestorServiceKafka {
	public String generateURL(String year, String month,String day,String nexrad,String fileName,String userid,String sessionid,String requestid) throws Exception 
	{
		
		DataIngestorBean dataIngestorBean = new DataIngestorBean(year, month,day,nexrad,userid,sessionid,requestid);
		fileName = fileName+".gz";
		String urllink = "http://noaa-nexrad-level2.s3.amazonaws.com/"+ dataIngestorBean.getYear()+"/"+dataIngestorBean.getMonth()+"/"+dataIngestorBean.getDay()+"/"+dataIngestorBean.getNexrad() +"/"+fileName;
		return urllink;
	}
}
