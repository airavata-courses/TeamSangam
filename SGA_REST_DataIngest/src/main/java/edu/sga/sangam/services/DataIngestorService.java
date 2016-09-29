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

	public DataIngestorStatusBean generateURL(String year, String month,String day,String nexrad,String fileName) throws Exception 
	{
		DataIngestorBean dataIngestorBean = new DataIngestorBean(year, month,day,nexrad);
		fileName = fileName+".gz";
		
		
		String urllink = "http://noaa-nexrad-level2.s3.amazonaws.com/"+ dataIngestorBean.getYear()+"/"+dataIngestorBean.getMonth()+"/"+dataIngestorBean.getDay()+"/"+dataIngestorBean.getNexrad() +"/"+fileName;
		
		//String urllink = "https://aws.amazon.com/noaa-big-data/"+ dataIngestorBean.getYear()+"/"+dataIngestorBean.getMonth()+"/"+dataIngestorBean.getDay()+"/"+dataIngestorBean.getNexrad() +"/"+fileName;
		//String urllink ="http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/03/KABX/KABX20150303_001050_V06.gz"	;
		System.out.println(urllink);
		try
		{
			//System.out.println("inside tyr block");
			String fileName1 = FilenameUtils.getBaseName(urllink);
			String extension = FilenameUtils.getExtension(urllink);
			String completeFileName =year+month+day+nexrad+fileName1;
			//System.out.println("calling chekc file method");
			boolean fileExists = DBOperations.getInstance().checkFile(year+month+day+nexrad+fileName1);
			//System.out.println(fileExists);
			if(fileExists)
			{
				//System.out.println("file exists");
				return new DataIngestorStatusBean(fileExists,urllink);
			}
			else
			{	//ystem.out.println("inside");
				File tmpfile = File.createTempFile(fileName,"."+extension);
				//System.out.println("before url");
				URL url = new URL(urllink);
				//System.out.println("after url");
				try
				{
				FileUtils.copyURLToFile(url,tmpfile);
				}catch(IOException e)
				{
					throw new IOException("There is no Data associated with the url provided");
				}
				System.out.println(tmpfile.length());
				if(tmpfile.length() !=0)
				{
					//System.out.println("file length greater than 0");
					boolean fileStatus =DBOperations.getInstance().insertFile(tmpfile,completeFileName);
					logger.info("file inserted status: "+fileStatus);
					tmpfile.deleteOnExit();
					return  new DataIngestorStatusBean(fileExists,urllink);

				}
				else
				{
					//System.out.println("no file");
					throw new IOException("There is no weather forecast file available for the date you requested");
				}
			}
		}catch(Exception e)
		{
			//System.out.println("some thing going fishy");
			throw e;		
		}

	}

}
