package edu.sga.sangam.resources;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	 static private int portNumber;
	    static private String ipaddress;
		private static final String endpointURI = "SGA_REST_Forecast/sga/runforecast";
		private static String serviceName =null;
		public static void main(String[] args) throws Exception {
			if (args.length != 3) {
	            throw new IllegalArgumentException("Invalid arguments");
	        }
				
			serviceName = args[0];
			ipaddress = args[1];
			portNumber = Integer.parseInt(args[2]);
			String url =String.format( "http://%s:%d/%s",
					ipaddress,
					portNumber,
					endpointURI);
			ZooKeeperService services = new ZooKeeperService();
			services.registerService(serviceName,url);
			
		}
	
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getForecast(@FormDataParam("file") InputStream input,@FormDataParam("file") FormDataContentDisposition fileDetail,@FormDataParam("userid") String userid,
			@FormDataParam("sessionid") String sessionid,@FormDataParam("requestid") String requestid) throws Exception
	{
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		JSONObject runforecast = new JSONObject();
		runforecast.put("userid", userid);
		runforecast.put("sessionid",sessionid);
		runforecast.put("requestid",requestid);
		runforecast.put("requestData", "Requested to return json data for the given input file");
		
		runforecast.put("requestTime", df2.format(date));
		if (input == null || fileDetail == null ){
			runforecast.put("responseData", "Response File is empty");
			runforecast.put("responseTime", df2.format(date));
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
			runforecast.put("responseTime", df2.format(date));
			registry(runforecast);
			return Response.status(200).entity(jsonString).build();
		
	}catch(Exception e)
		{
		runforecast.put("responseData","Error in generating coordinates ");
		runforecast.put("responseTime", df2.format(date));
		registry(runforecast);
		return Response.status(500).entity("Error in generating coordinates").build();
		}
	}
	
	public void registry(JSONObject runforecast) throws Exception
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://54.209.48.186:8085/SGA_REST_Registry/sga/resgitry/runforecastlogdata");
		StringRequestEntity entity = new StringRequestEntity(runforecast.toJSONString(),"application/json","UTF-8");
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		try
		{
		int statusCode = client.executeMethod(post);
		}
		catch(Exception e)
		{
			throw new Exception("issue  with forecast registry");
		}
		
		
	}

}
