package edu.sga.sangam.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import edu.sga.sangam.bean.ClientOrchestratorBean;
import edu.sga.sangam.bean.DataIngestorBean;
import edu.sga.sangam.bean.StormClusterBean;
import edu.sga.sangam.bean.StormDetectionBean;

public class WeatherClientOrchestrator {
	ClientOrchestratorBean cob = null;
	DataIngestorBean dib = null;
	StormClusterBean scb=null;
	StormDetectionBean sdb = null;
	
	
	public String clientOrchestrator(String year,String mm,String day,String nexrad,String fileName,String userid,String sessionid,
			String requestid) throws Exception
	{
		
		cob = new ClientOrchestratorBean(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
		try
		{

			callDataIngestor(year,mm,day,nexrad,fileName);
			callStormCluster(dib.getUrl());
			callStormDetection(scb.getFile(),cob.getNexrad());
			String decision =callForecastDecision();
			if(decision.equals("yes"))
			{
				String finalresult = callrunForecast();
				return finalresult;
			}
			else
			{
				return "no";
			}
			
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

	}

	public void callDataIngestor(String year,String mm,String day,String nexrad,String fileName) throws Exception
	{
		HttpClient client = new HttpClient();
		String dataIngestorURL = "http://sgadataingest:8081/SGA_REST_DataIngest/sga/dataingestor";
		GetMethod getMethod = new GetMethod(dataIngestorURL);
		try{

			NameValuePair  yearParam = new NameValuePair("year",URIUtil.encodeQuery(year));
			NameValuePair monthParam = new NameValuePair("month",URIUtil.encodeQuery(mm));
			NameValuePair dayParam = new NameValuePair("day",URIUtil.encodeQuery(day));
			NameValuePair stationParam = new NameValuePair("nexrad",URIUtil.encodeQuery(nexrad));
			NameValuePair filenameParam = new NameValuePair("filename",URIUtil.encodeQuery(fileName));
			NameValuePair useridParam = new NameValuePair("userid",URIUtil.encodeQuery(cob.getUserid()));
			NameValuePair sessionidParam = new NameValuePair("sessionid",URIUtil.encodeQuery(cob.getSessionid()));
			NameValuePair requestidParam = new NameValuePair("requestid",URIUtil.encodeQuery(cob.getRequestid()));
			NameValuePair[] params = new NameValuePair[] {yearParam, monthParam,dayParam,stationParam,filenameParam,useridParam,sessionidParam,requestidParam};
			getMethod.setQueryString(params);
			int response = client.executeMethod(getMethod);
			System.out.println("service response is "+response);
			if(response ==200)
			{
				String  jsonStr = getMethod.getResponseBodyAsString();
				JSONParser parser = new JSONParser();
				try {
					Object obj = parser.parse(jsonStr);
					JSONObject jsonObject = (JSONObject) obj;
					String url = ((String) jsonObject.get("url"));
					System.out.println("url link is "+url);
					boolean fileexists = ((boolean) jsonObject.get("fileExists"));
					dib = new DataIngestorBean(response,fileexists,url);
				} catch (ParseException e) {
					//e.printStackTrace(); 
					throw new Exception("Error in parsing the response string");
				}
			}
			else
			{
				String jsonStr = getMethod.getResponseBodyAsString();
				System.out.println(jsonStr);
				JSONParser parser = new JSONParser();

				throw new IOException("Status Code is "+response+"\n ErrorMessage: "+jsonStr);

			}
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}

	public void callStormCluster(String url) throws Exception
	{
		String stormclusteringURL ="http://sgastormclustering:8083/SGA_REST_StormClustering/sga/stormclustering";
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod(stormclusteringURL);
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair useridParam = new NameValuePair("userid",URIUtil.encodeQuery(cob.getUserid()));
		NameValuePair sessionidParam = new NameValuePair("sessionid",URIUtil.encodeQuery(cob.getSessionid()));
		NameValuePair requestidParam = new NameValuePair("requestid",URIUtil.encodeQuery(cob.getRequestid()));
		NameValuePair[] params = new NameValuePair[] {useridParam,sessionidParam,requestidParam};
		post.setQueryString(params);
		post.setParameter("url",url );
		try{
			int response = client.executeMethod(post);
			if(response ==200){
				InputStream in =post.getResponseBodyAsStream();
				byte[] bytes = IOUtils.toByteArray(in);
				/*File targetfile = new File("/Users/ramarvab/Desktop/xyz.kml");
			targetfile.createNewFile();*/
				File targetfile = File.createTempFile(cob.getFileName(),".kml");
				FileOutputStream fos = new FileOutputStream(targetfile);
				fos.write(bytes);
				fos.close();

				scb = new StormClusterBean(response,targetfile);			
			}
			else
			{
				String jsonStr = post.getResponseBodyAsString();
				throw new IOException("Status code is "+response+"\n ErrorMessage: "+jsonStr);
			}
		}catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

	}

	public void callStormDetection(File file,String station) throws Exception
	{
		String stormDetectionURL ="http://sgastormdetection:8082/SGA_REST_StormDetection/sga/stormdetection";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpEntity entity = MultipartEntityBuilder.create().addTextBody("station", station)
				.addBinaryBody("file",file,ContentType.create("application/octet-stream"),cob.getFileName())
				.addTextBody("userid", cob.getUserid())
				.addTextBody("sessionid", cob.getSessionid())
				.addTextBody("requestid",cob.getRequestid())
				.build();
		
		List<NameValuePair> postParameters= new ArrayList<NameValuePair>();
		
		HttpPost httpPost = new HttpPost(stormDetectionURL);
		httpPost.setEntity(entity);
		
		try
		{
			HttpResponse response = httpclient.execute(httpPost);
			int status =response.getStatusLine().getStatusCode();
			if(status ==200)
			{
				InputStream is = response.getEntity().getContent();
				byte[] bytes = IOUtils.toByteArray(is);
				/*File targetfile = new File("/Users/ramarvab/Desktop/pqr.kml");
			targetfile.createNewFile();*/
				File targetfile = File.createTempFile(cob.getFileName(),".Kml");
				FileOutputStream fos = new FileOutputStream(targetfile);
				fos.write(bytes);
				fos.close();
				sdb = new StormDetectionBean(status,targetfile);

			}
			else
			{
				String jsonStr = EntityUtils.toString(response.getEntity());
				throw new IOException("Status code is "+response+"\n ErrorMessage: "+jsonStr);
			}
		}
		catch(Exception e)
		{
			throw new Exception("Something went wrong with Stormcluster Service");
		}

	}

	public String callForecastDecision() throws Exception
	{
		HttpClient client = new HttpClient();
		String dataIngestorURL = "http://sgaforecast:8084/SGA_REST_ForecastDecision/sga/forecastdecision";
		GetMethod getMethod = new GetMethod(dataIngestorURL);
		NameValuePair useridParam = new NameValuePair("userid",URIUtil.encodeQuery(cob.getUserid()));
		NameValuePair sessionidParam = new NameValuePair("sessionid",URIUtil.encodeQuery(cob.getSessionid()));
		NameValuePair requestidParam = new NameValuePair("requestid",URIUtil.encodeQuery(cob.getRequestid()));
		NameValuePair[] params = new NameValuePair[] {useridParam,sessionidParam,requestidParam};
		getMethod.setQueryString(params);
		try{
			int response = client.executeMethod(getMethod);
			if(response ==200)
			{	
				String  decision = getMethod.getResponseBodyAsString();
				return decision;
			}
			else
			{
				//String jsonStr = response.getEntity().toString();
				String jsonStr = getMethod.getResponseBodyAsString();
				throw new IOException("Status code is "+response+"\n ErrorMessage: "+jsonStr);
			}
		}catch(Exception e)
		{
			throw new Exception("Something went wrong with ForecastDecision  Service");
		}

	}

	public String callrunForecast() throws Exception
	{
		String runForecasturl ="http://sgaforecast:8084/SGA_REST_Forecast/sga/runforecast";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpEntity entity = MultipartEntityBuilder.create()
				.addBinaryBody("file",sdb.getFile(),ContentType.create("application/octet-stream"),cob.getFileName())
				.addTextBody("userid", cob.getUserid())
				.addTextBody("sessionid", cob.getSessionid())
				.addTextBody("requestid",cob.getRequestid())
				.build();
		HttpPost httpPost = new HttpPost(runForecasturl);
		httpPost.setEntity(entity);
		try
		{
			HttpResponse response = httpclient.execute(httpPost);
			int status =response.getStatusLine().getStatusCode();
			if(status ==200)
			{
				String json = EntityUtils.toString(response.getEntity());
				return json;
			}
			else
			{
				String jsonStr = EntityUtils.toString(response.getEntity());
				throw new IOException("Status code is "+response+"\n ErrorMessage: "+jsonStr);
			}
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

	}

}
