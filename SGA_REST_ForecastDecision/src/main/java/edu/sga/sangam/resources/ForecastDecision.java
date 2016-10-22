package edu.sga.sangam.resources;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.simple.JSONObject;

@Path("forecastdecision")
public class ForecastDecision {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDecision(@QueryParam("userid") String userid,
			@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid) throws Exception
	{
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		JSONObject forecastdecision = new JSONObject();
		forecastdecision.put("userid", userid);
		forecastdecision.put("sessionid",sessionid);
		forecastdecision.put("requestid",requestid);
		forecastdecision.put("requestData", "Requested for decision to run forecast algorithm or not");
		Timestamp requestTime = new Timestamp(date.getTime());
		forecastdecision.put("requestTime", df2.format(date));
		Random r = new Random();
		String decision =null;
		int random = r.nextInt();
		if(random<0.25)
		{
			
			decision= "no";
		}
		else
		{
			decision= "yes";
		}
		Timestamp responseTime = new Timestamp(date.getTime());
		
		forecastdecision.put("responseData", "Decision is "+decision);
		forecastdecision.put("responseTime", df2.format(date));
		registry(forecastdecision);
		return decision;
	}
	public void registry(JSONObject forecastdecision) throws Exception
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://sgaregistry:8085/SGA_REST_Registry/sga/resgitry/decisionlogdata");
		StringRequestEntity entity = new StringRequestEntity(forecastdecision.toJSONString(),"application/json","UTF-8");
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		try
		{
		int statusCode = client.executeMethod(post);
		}
		catch(Exception e)
		{
			throw new Exception("issue  with forecast decision registry");
		}
		
		
	}

}
