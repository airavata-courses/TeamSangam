package org.sciencegateway.services.dataingestor.resources;

import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.sciencegateway.services.dataingestor.dataingestorservice.UrlService;

@Path("/generateurl")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UrlGenerator {
	private final Logger log = LoggerFactory.getLogger(UrlGenerator.class);
	UrlService urlservice = new UrlService();
	@GET
	public Response getURL(@QueryParam("year") int year,@QueryParam("month") int mm,
			@QueryParam("day") int day,@QueryParam("nexrad") String nexrad)
	{
		try
		{
		log.info("year:" +year +"month:"+mm+"day:"+day+"nexrad:"+nexrad);
		return Response.status(200).entity(urlservice.generateURL(year, mm, day, nexrad)).build();
		}catch(Exception e)
		{
			return Response.status(400).entity("error in generating url").build();
		}
	}

}
