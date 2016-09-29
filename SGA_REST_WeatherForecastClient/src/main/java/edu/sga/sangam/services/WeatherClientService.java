package edu.sga.sangam.services;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.Header;
//import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.util.IOUtils;

public class WeatherClientService {
	public static void main(String[] args) throws HttpException, IOException {
		//String weatherForecastURL = "http://localhost:8084/SGA_REST_ForecastDecision/sga/forecastdecision";
		String year ="2005";
		String month ="03";
		String day ="03";
		String station ="KABX";
		String fileName ="KABX20150303_001050_V06";
		String dataIngestorURL = "http://localhost:8080/SGA_REST_DataIngest/sga/dataingestor";
		String stormclusteringURL ="http://localhost:8080/SGA_Rest_StormClustering/sga/stormclustering";
		/*HttpClient client = new HttpClient();
		String url1="http://noaa-nexrad-level2.s3.amazonaws.com/2005/03/03/KABX/KABX20150303_001050_V06.gz";
		PostMethod post = new PostMethod(stormclusteringURL);
        post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setParameter("url",url1 );
		int response = client.executeMethod(post);
		
		InputStream in =post.getResponseBodyAsStream();
		byte[] bytes = IOUtils.toByteArray(in);
		File targetfile = File.createTempFile(fileName,".kml");
		targetfile.createNewFile();
		FileOutputStream fos = new FileOutputStream(targetfile);
		fos.write(bytes);
		System.out.println("file written successfully");*/
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		File file = new File("/Users/ramarvab/Desktop/abcd.kml");
		HttpEntity entity = MultipartEntityBuilder.create()
				.addBinaryBody("file",file,ContentType.create("application/octet-stream"),fileName).build();
		HttpPost httpPost = new HttpPost("http://localhost:8084/SGA_Rest_RUNFORECAST/sga/runforecast");
		httpPost.setEntity(entity);
		HttpResponse response = httpclient.execute(httpPost);
		//String responseString = EntityUtils.toString(entity1, "UTF-8");	
		//System.out.println(responseString);
		System.out.println(response.getStatusLine().getStatusCode());
		//InputStream is = response.getEntity().getContent();
		//String body = IOUtils.toString(is);
		//System.out.println(body);
		String json = EntityUtils.toString(response.getEntity());
		System.out.println(json);
		//System.out.println(response.getEntity().toString());
		/*
		InputStream is = response.getEntity().getContent();
		//InputStream is = (InputStream)response.getEntity();
		byte[] bytes = IOUtils.toByteArray(is);
		File targetfile = new File("/Users/ramarvab/Desktop/abcd.kml");
		
		targetfile.createNewFile();
		FileOutputStream fos = new FileOutputStream(targetfile);
		fos.write(bytes);
		fos.close();
		System.out.println("file written successfully");*/
		
		
		
		
		
		
		/*GetMethod getMethod = new GetMethod(dataIngestorURL);
		try{
			
		NameValuePair  yearParam = new NameValuePair("year",URIUtil.encodeQuery(year));
		NameValuePair monthParam = new NameValuePair("month",URIUtil.encodeQuery(month));
		NameValuePair dayParam = new NameValuePair("day",URIUtil.encodeQuery(day));
		NameValuePair stationParam = new NameValuePair("nexrad",URIUtil.encodeQuery(station));
		NameValuePair filenameParam = new NameValuePair("filename",URIUtil.encodeQuery(fileName));
		NameValuePair[] params = new NameValuePair[] {yearParam, monthParam,dayParam,stationParam,filenameParam};
		getMethod.setQueryString(params);
		
		int response = client.executeMethod(getMethod);
		System.out.println(response);
		if(response ==200)
		{
			String  jsonStr = getMethod.getResponseBodyAsString();
			JSONParser parser = new JSONParser();
			 try {
	                Object obj = parser.parse(jsonStr);
	                JSONObject jsonObject = (JSONObject) obj;
	                String url = ((String) jsonObject.get("url"));
	                boolean fileexists =((boolean) jsonObject.get("fileExists"));
	                System.out.println(url);
	                System.out.println(fileexists);

	            } catch (ParseException e) {
	                e.printStackTrace(); 
	            }
		}
			 else
		{
			String jsonStr = getMethod.getResponseBodyAsString();
            throw new IOException("Status Code is "+response+"\n ErrorMessage: "+jsonStr);
            
		}
		
		
		
		
		
	}catch(URIException e)
		{
		e.printStackTrace();
		}*/
		
		
	
	}
}

