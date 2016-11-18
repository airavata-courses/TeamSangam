package edu.sga.sangam.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.sga.sangam.db.DBOperations;

@Path("/registry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Registry {
	
	static private int portNumber;
	static private String ipaddress;
	private static final String endpointURI = "SGA_REST_Registry/sga/registry";
	private static String serviceName =null;
	private static String hostIP =null;
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
			return Response.status(500).entity("issue with database").build();
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
	
	@Path("/getstats")
    @GET
    public Response GetStatistics(){
	    try
        {
	    String result = DBOperations.getInstance().getStats();
        return Response.status(200).entity(result).build();
        }
        catch(Exception e)
        {
            return Response.status(500).entity(e.getMessage()).build();
        }
        
    }
	
	

}
