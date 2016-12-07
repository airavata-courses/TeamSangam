package edu.sga.sangam.client;

import javax.ejb.Asynchronous;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import edu.sga.sangam.services.WeatherClientOrchestrator;


@Path("weatherclient")
public class WeatherClient {
	WeatherClientOrchestrator wcs = new WeatherClientOrchestrator();
	@GET
	@Asynchronous
	
	public void asyncrest(@Suspended final AsyncResponse asyncResponse,@QueryParam("year") String year,@QueryParam("month") String mm,
			@QueryParam("day") String day,@QueryParam("nexrad") String nexrad,@QueryParam("filename") String fileName,@QueryParam("userid") String userid,
			@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid)
	{
		Response result = getURL(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
		asyncResponse.resume(result);
	}
	public Response getURL(String year,String mm,String day,String nexrad,String fileName,String userid,String sessionid,String requestid)
	{
		try{
			
			String output =wcs.clientOrchestrator(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
			return Response.status(200).entity(output).build();
			 
		}catch(Exception e)
		{
			return Response.status(400).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("result")
	@Asynchronous
	public void asyncresultrest(@Suspended final AsyncResponse asyncResponse,@QueryParam("key") String key)
	{
		Response result = getResult(key);
		asyncResponse.resume(result);	
	}
	
	public Response getResult(String key)
	{
		try
		{
			String output =wcs.getResultFromRegistry(key);
			return Response.status(200).entity(output).build();
		}catch(Exception e)
		{
			return Response.status(400).entity(e.getMessage()).build();
		}
	}
	

}
