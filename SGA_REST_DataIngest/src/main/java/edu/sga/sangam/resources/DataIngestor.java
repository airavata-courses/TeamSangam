package edu.sga.sangam.resources;

import java.io.IOException;
import org.apache.curator.framework.CuratorFramework;


import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sga.sangam.bean.DataIngestorStatusBean;
import edu.sga.sangam.services.DataIngestorService;



@Path("/dataingestor")
@Produces(MediaType.APPLICATION_JSON)
public class DataIngestor {
    static private int portNumber;
	private static final String endpointURI = "SGA_REST_DataIngest/sga/dataingestor";
	private static String serviceName =null, String hostIP=null;
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
            throw new IllegalArgumentException("Invalid arguments");
        }
		hostIP = args[0];
		serviceName = args[1];
		portNumber = Integer.parseInt(args[2]);
		String url =String.format( "http://%s:%d/%s",
				hostIP,
				portNumber,
				endpointURI);
		ZooKeeperService services = new ZooKeeperService();
		services.registerService(serviceName,url);
		
	}
	
	private final Logger log = LoggerFactory.getLogger(DataIngestor.class);
	DataIngestorService urlservice = new DataIngestorService();
	
	@GET
	public Response getURL(@QueryParam("year") String year,@QueryParam("month") String mm,
			@QueryParam("day") String day,@QueryParam("nexrad") String nexrad,@QueryParam("filename") String fileName,
			@QueryParam("userid") String userid,@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid) throws Exception
	{
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		JSONObject requestDataIngestor = new JSONObject();
		requestDataIngestor.put("userid", userid);
		requestDataIngestor.put("sessionid",sessionid);
		requestDataIngestor.put("requestid",requestid);
		requestDataIngestor.put("requestData", "Requested Data for year"+year+ "month "+mm+" day "+day+ " Station "+nexrad+
				"filename "+fileName);
		requestDataIngestor.put("requestTime", df2.format(date));
		
		
		try
		{
			//System.out.println("inside data ingestor");
			log.info("year:" +year +"month:"+mm+"day:"+day+"nexrad:"+nexrad);
			DataIngestorStatusBean disb = urlservice.generateURL(year, mm, day, nexrad,fileName,userid,sessionid,requestid);
			
			requestDataIngestor.put("responseData", "Response returned is "+disb.getUrl());
			
			requestDataIngestor.put("responseTime", df2.format(date));
			registry(requestDataIngestor);
			
			return Response.status(200).entity(disb).build();
		}catch(Exception e)
		{
			//System.out.println("inside exception"+e.getMessage());
			requestDataIngestor.put("responseData", "Response returned is "+e.getMessage());
			
			requestDataIngestor.put("responseTime", df2.format(date));
			registry(requestDataIngestor);
			return Response.status(400).entity(e.getMessage()).build();
		}
	}
	
	public void registry(JSONObject requestDataIngestor) throws IOException 
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://54.209.48.186:8085/SGA_REST_Registry/sga/resgitry/dilogdata");
		StringRequestEntity entity;
		try {
			entity = new StringRequestEntity(requestDataIngestor.toJSONString(),"application/json","UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			throw new UnsupportedEncodingException("Issue with Encoding");
		}
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		
		
		int statusCode;
		try {
			statusCode = client.executeMethod(post);
			log.info("Data Ingestor Resigstry status code is "+statusCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw  new IOException("Registry not started");
		}
		
		
		
		
		
	}

}
