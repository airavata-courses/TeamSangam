package edu.sga.sangam.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;


public class RunForecastService {


	public String getForecastDetails(String fileName, byte[] bytes) throws IOException {
		File tmpfile= null;
		FileOutputStream fop =null;
		
		try
		{
			System.out.println("in service");
			tmpfile = File.createTempFile(fileName, ".kml");
			/*File tmpfile =new File("/Users/ramarvab/Desktop/tmp.txt");
			if(!tmpfile.exists())
			{
				tmpfile.createNewFile();
			}*/
			fop = new FileOutputStream(tmpfile);
			fop.write(bytes);
			
			InputStream in = new FileInputStream(tmpfile);
			String xml = IOUtils.toString(in);
			JSONObject xmlJSONObj = XML.toJSONObject(xml);
			String jsonString = xmlJSONObj.toString(4);
			return jsonString;
			
		}catch(Exception e)
		{
			throw new IOException("Error in Running weather forecast");
		}
		finally
		{
			fop.flush();
			fop.close();
		}
		
	}
}
