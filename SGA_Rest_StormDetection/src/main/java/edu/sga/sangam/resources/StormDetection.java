package edu.sga.sangam.resources;

import java.io.File;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sga.sangam.bean.StormDetectionResponseBean;
import edu.sga.sangam.services.StormDetectionService;



@Path("stormdetection")
public class StormDetection {
	private final Logger log = LoggerFactory.getLogger(StormDetection.class);
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response detectStormKML(@FormDataParam("file") InputStream input,@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("station") String station,@FormDataParam("userid") String userid,
			@FormDataParam("sessionid") String sessionid,@FormDataParam("requestid") String requestid ) throws Exception
	{
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		JSONObject stormdetection = new JSONObject();
		stormdetection.put("userid", userid);
		stormdetection.put("sessionid",sessionid);
		stormdetection.put("requestid",requestid);
		stormdetection.put("requestData", "Requested to generate KML file for the given input file");
		
		stormdetection.put("requestTime", df2.format(date));
		//System.out.println("input si"+input);
		if (input == null || fileDetail == null )
		{
			stormdetection.put("responseData", "Response is File is empty");
			stormdetection.put("responseTime", df2.format(date));
			registry(stormdetection);
			return Response.status(400).entity("File is empty").build();
		}
		StormDetectionService stormDetectionService = new StormDetectionService();
		try
		{
			String fileName = fileDetail.getFileName();
			File kmlfile = stormDetectionService.generateKMLFile(fileName,station);
			stormdetection.put("responseData", "Response is a file with filename "+fileName);
			Timestamp responseTime = new Timestamp(date.getTime());
			stormdetection.put("responseTime", responseTime);
			registry(stormdetection);
			return Response.ok(kmlfile).header("Content-Disposition", "attachment; filename=\"" +fileName+"_cluster.kml"+ "\"").
					build();
			
		}
		catch(Exception e)
		{
			stormdetection.put("responseData","Error in generating Storm clustering file ");
			
			stormdetection.put("responseTime", df2.format(date));
			registry(stormdetection);
			return Response.status(500).entity("Error in generating Storm clustering file").build();
		}
		
	}
	
	public void registry(JSONObject stormdetection) throws Exception
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://localhost:8080/SGA_REST_Registry/sga/resgitry/sdlogdata");
		StringRequestEntity entity = new StringRequestEntity(stormdetection.toJSONString(),"application/json","UTF-8");
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		try
		{
		int statusCode = client.executeMethod(post);
		}
		catch(Exception e)
		{
			throw new Exception("issue with storm detection registry");
		}
		
		
	}
}
