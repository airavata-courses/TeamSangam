package edu.sga.sangam.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import edu.sga.sangam.services.WeatherClientOrchestrator;


@Path("weatherclient")
public class WeatherClient {
	
	@GET
	public Response getURL(@QueryParam("year") String year,@QueryParam("month") String mm,
			@QueryParam("day") String day,@QueryParam("nexrad") String nexrad,@QueryParam("filename") String fileName,@QueryParam("userid") String userid,
			@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid)
	{
		try
		{
			WeatherClientOrchestrator wcs = new WeatherClientOrchestrator();
			String output =wcs.clientOrchestrator(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
			return Response.status(200).entity(output).build();
		}catch(Exception e)
		{
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

}
