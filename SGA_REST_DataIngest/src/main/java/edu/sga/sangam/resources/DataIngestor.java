package edu.sga.sangam.resources;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
	
	private final Logger log = LoggerFactory.getLogger(DataIngestor.class);
	DataIngestorService urlservice = new DataIngestorService();
	
	@GET
	public Response getURL(@QueryParam("year") String year,@QueryParam("month") String mm,
			@QueryParam("day") String day,@QueryParam("nexrad") String nexrad,@QueryParam("filename") String fileName,
			@QueryParam("userid") String userid,@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid) throws HttpException, IOException
	{
		JSONObject requestDataIngestor = new JSONObject();
		requestDataIngestor.put("userid", userid);
		requestDataIngestor.put("sessionid",sessionid);
		requestDataIngestor.put("requestid",requestid);
		requestDataIngestor.put("requestData", "Requested Data for year"+year+ "month "+mm+" day "+day+ " Station "+nexrad+
				"filename "+fileName);
		try
		{
			//System.out.println("inside data ingestor");
			log.info("year:" +year +"month:"+mm+"day:"+day+"nexrad:"+nexrad);
			DataIngestorStatusBean disb = urlservice.generateURL(year, mm, day, nexrad,fileName,userid,sessionid,requestid);
			requestDataIngestor.put("responseData", "Response returned is "+disb.getUrl());
			registry(requestDataIngestor);
			
			return Response.status(200).entity(disb).build();
		}catch(Exception e)
		{
			//System.out.println("inside exception"+e.getMessage());
			requestDataIngestor.put("responseData", "Response returned is "+e.getMessage());
			registry(requestDataIngestor);
			return Response.status(400).entity(e.getMessage()).build();
		}
	}
	
	public void registry(JSONObject requestDataIngestor) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://localhost:8080/SGA_REST_Registry/sga/resgitry/dilogdata");
		StringRequestEntity entity = new StringRequestEntity(requestDataIngestor.toJSONString(),"application/json","UTF-8");
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		int statusCode = client.executeMethod(post);
		log.info("Data Ingestor Resigstry status code is "+statusCode);
		
	}

}
