package edu.sga.sangam.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sga.sangam.services.DataIngestorService;


@Path("/dataingestor")
@Produces(MediaType.APPLICATION_JSON)
public class DataIngestor {
	
	private final Logger log = LoggerFactory.getLogger(DataIngestor.class);
	DataIngestorService urlservice = new DataIngestorService();
	
	@GET
	public Response getURL(@QueryParam("year") int year,@QueryParam("month") int mm,
			@QueryParam("day") int day,@QueryParam("nexrad") String nexrad)
	{
		try
		{
			//System.out.println("inside data ingestor");
			log.info("year:" +year +"month:"+mm+"day:"+day+"nexrad:"+nexrad);

			return Response.status(200).entity(urlservice.generateURL(year, mm, day, nexrad)).build();
		}catch(Exception e)
		{
		
			return Response.status(400).entity("There is no weather forecast file available for the date you requested").build();
		}
	}

}
