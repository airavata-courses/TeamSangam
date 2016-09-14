package org.sciencegateway.services.dataingestor.dataingestorservice;

import org.sciencegateway.services.dataingestor.dataingestormodal.UrlModal;

public class UrlService {
	
	public String generateURL(int year, int month,int day,String nexrad)
	{
		UrlModal urlmodal = new UrlModal(year, month, day ,nexrad);
		String fileName = String.valueOf(urlmodal.getYear())+String.valueOf(urlmodal.getMonth())+String.valueOf(urlmodal.getDay())+urlmodal.getNexrad()+".gz";
		String url = "https://aws.amazon.com/noaa-big-data/"+ urlmodal.getNexrad() +"/"+urlmodal.getYear()+"/"+urlmodal.getMonth()+"/"+urlmodal.getDay()+"/"+fileName;
		return  url;
	}
}
