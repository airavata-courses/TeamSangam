package edu.sga.sangam.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.sga.sangam.db.DBOperations;

@Path("/resgitry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Registry {
	
	@Path("/dilogdata")
	@POST
	public Response dataIngestorRequest(DataIngestRequest input)
	{
		try
		{
		DBOperations.getInstance().dataIngestRequest(input);
		return Response.status(200).entity(input.getUserid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	@Path("/sclogdata")
	@POST
	public Response StormCluster(StormClusterBean scb)
	{
		try
		{
		DBOperations.getInstance().stormCluster(scb);
		return Response.status(200).entity(scb.getUserid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Path("/sdlogdata")
	@POST
	public Response StormDetection(StormDetectionBean sdb)
	{
		try
		{
		DBOperations.getInstance().stormDetection(sdb);
		return Response.status(200).entity(sdb.getUserid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Path("/decisionlogdata")
	@POST
	public Response ForecastDecision(ForecastDecisionBean fdb)
	{
		try
		{
		DBOperations.getInstance().forecastDecision(fdb);
		return Response.status(200).entity(fdb.getUserid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Path("/runforecastlogdata")
	@POST
	public Response RunForecast(RunForecastBean rfb)
	{
		try
		{
		DBOperations.getInstance().runForecast(rfb);
		return Response.status(200).entity(rfb.getUserid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	

}
