package edu.sga.sangam.resources;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;

import edu.sga.sangam.services.RunForecastService;
@Path("runforecast")
public class RunForecast {
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getForecast(@FormDataParam("file") InputStream input,@FormDataParam("file") FormDataContentDisposition fileDetail,@FormDataParam("userid") String userid,
			@FormDataParam("sessionid") String sessionid,@FormDataParam("requestid") String requestid) throws Exception
	{
		JSONObject runforecast = new JSONObject();
		runforecast.put("userid", userid);
		runforecast.put("sessionid",sessionid);
		runforecast.put("requestid",requestid);
		runforecast.put("requestData", "Requested to return json data for the given input file");
		if (input == null || fileDetail == null ){
			runforecast.put("responseData", "Response File is empty");
			registry(runforecast);
			return Response.status(400).entity("File is empty").build();
		}
		RunForecastService runForecastService = new RunForecastService();
		try{
			System.out.println("running forecast");
			String fileName = fileDetail.getFileName();
			byte[] bytes = IOUtils.toByteArray(input);
			String jsonString =runForecastService.getForecastDetails(fileName,bytes);
			runforecast.put("responseData", "Response is a jsonstring "+jsonString);
			registry(runforecast);
			return Response.status(200).entity(jsonString).build();
		
	}catch(Exception e)
		{
		runforecast.put("responseData","Error in generating coordinates ");
		registry(runforecast);
		return Response.status(500).entity("Error in generating coordinates").build();
		}
	}
	
	public void registry(JSONObject runforecast) throws Exception
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://localhost:8080/SGA_REST_Registry/sga/resgitry/runforecastlogdata");
		StringRequestEntity entity = new StringRequestEntity(runforecast.toJSONString(),"application/json","UTF-8");
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		try
		{
		int statusCode = client.executeMethod(post);
		}
		catch(Exception e)
		{
			throw new Exception("issue  with storm detection registry");
		}
		
		
	}

}
