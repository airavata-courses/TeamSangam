package edu.sga.sangam.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.sga.sangam.bean.DataIngestorBean;
import edu.sga.sangam.bean.DataIngestorStatusBean;
import edu.sga.sangam.db.DBOperations;

public class DataIngestorService {

	private static Logger logger = Logger.getLogger(DBOperations.class);

	public DataIngestorStatusBean generateURL(int year, int month,int day,String nexrad) throws Exception 
	{
		DataIngestorBean dataIngestorBean = new DataIngestorBean(year, month,day,nexrad);
		String fileName = String.valueOf(dataIngestorBean.getYear())+String.valueOf(dataIngestorBean.getMonth())+String.valueOf(dataIngestorBean.getDay())+dataIngestorBean.getNexrad()+".gz";
		String urllink = "https://aws.amazon.com/noaa-big-data/"+ dataIngestorBean.getNexrad() +"/"+dataIngestorBean.getYear()+"/"+dataIngestorBean.getMonth()+"/"+dataIngestorBean.getDay()+"/"+fileName;
		//String urllink ="http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/03/KABX/KABX20150303_001050_V06.gz"	;
		try
		{
			String fileName1 = FilenameUtils.getBaseName(urllink);
			String extension = FilenameUtils.getExtension(urllink);
			boolean fileExists = DBOperations.getInstance().checkFile(fileName1);
			if(fileExists)
			{
				return new DataIngestorStatusBean(fileExists,urllink);
			}
			else
			{
				File tmpfile = File.createTempFile(fileName,"."+extension);
				URL url = new URL(urllink);
				FileUtils.copyURLToFile(url,tmpfile);

				if(tmpfile.length() !=0)
				{
					boolean fileStatus =DBOperations.getInstance().insertFile(tmpfile,fileName1);
					logger.info("file inserted status: "+fileStatus);
					tmpfile.deleteOnExit();
					return  new DataIngestorStatusBean(fileExists,urllink);

				}
				else
				{
					throw new IOException("There is no weather forecast file available for the date you requested");
				}
			}
		}catch(Exception e)
		{
			throw e;		
		}

	}

}
