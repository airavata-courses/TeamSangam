package edu.sga.sangam.resources;

import java.util.concurrent.TimeUnit;

import javax.ejb.Asynchronous;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.sga.sangam.db.DBOperations;

@Path("/registry")

public class Registry {
	// int inc =0;
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
		//services.discoverServiceURI("registry");
		services.registerService(serviceName,url);
		
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/dataingestor")
	@POST
	public Response dataIngestorRequest(DataIngestRequest input)
	{
		try
		{
		DBOperations.getInstance().dataIngestRequest(input);
		return Response.status(200).entity(input.getKey()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity("issue with database").build();
		}
		
	}
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/stormcluster")
	@POST
	public Response StormCluster(StormClusterBean scb)
	{
		try
		{
		DBOperations.getInstance().stormCluster(scb);
		return Response.status(200).entity(scb.getKey()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	@Path("/stormdetection")
	@POST
	public Response StormDetection(StormDetectionBean sdb)
	{
		try
		{
		DBOperations.getInstance().stormDetection(sdb);
		return Response.status(200).entity(sdb.getKey()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/forecast")
	@POST
	public Response ForecastDecision(ForecastDecisionBean fdb)
	{
		try
		{
		DBOperations.getInstance().forecastDecision(fdb);
		return Response.status(200).entity(fdb.getKey()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/mesosstatus")
	@POST
	public Response MesosStatus(MesosStatus ms)
	{
		try
		{
		DBOperations.getInstance().mesosstatus(ms);
		return Response.status(200).entity(ms.getKeyid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	@Path("/runforecast")
	@POST
	public Response RunForecast(RunForecastBean rfb)
	{
		try
		{
		DBOperations.getInstance().runForecast(rfb);
		return Response.status(200).entity(rfb.getKey()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/orchestrator")
	@POST
	
	public Response orchestrator(OrchestratorBean ob)
	{
		try
		{
		DBOperations.getInstance().orchestratorcollection(ob);
		return Response.status(200).entity(ob.getUserid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	
	@Path("/result")
	@POST
	
	public Response result(ResultBean rb)
	{
		try
		{
		DBOperations.getInstance().resultcollection(rb);
		return Response.status(200).entity(rb.getKeyid()).build();
		}
		catch(Exception e)
		{
			return Response.status(500).entity(e.getMessage()).build();
		}
		
	}
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/getstats")
    @GET
    public Response GetStatistics(){
        try
        {
        String result = DBOperations.getInstance().getStats();
        return Response.status(200).entity(result)
					.header("Access-Control-Allow-Origin", "http://ec2-54-193-116-150.us-west-1.compute.amazonaws.com:8080")
					.header("Access-Control-Allow-Methods", "GET")
					.header("Access-Control-Allow-Credentials", "true")
					.build();
        }
        catch(Exception e)
        {
            return Response.status(500).entity(e.getMessage())
					.header("Access-Control-Allow-Origin", "http://ec2-54-193-116-150.us-west-1.compute.amazonaws.com:8080")
					.header("Access-Control-Allow-Methods", "GET")
					.header("Access-Control-Allow-Credentials", "true")
					.build();
        }
        
    }
    

    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    @Path("/getuserstats")
    @GET
    public Response GetuserStats(@QueryParam("email") String username){
        try
        {
        String result = DBOperations.getInstance().getuserStats(username);
        return Response.status(200).entity(result)
					.header("Access-Control-Allow-Origin", "http://ec2-54-193-116-150.us-west-1.compute.amazonaws.com:8080")
					.header("Access-Control-Allow-Methods", "GET")
					.header("Access-Control-Allow-Credentials", "true")
					.build();
        }
        catch(Exception e)
        {
            return Response.status(500).entity(e.getMessage())
					.header("Access-Control-Allow-Origin", "http://ec2-54-193-116-150.us-west-1.compute.amazonaws.com:8080")
					.header("Access-Control-Allow-Methods", "GET")
					.header("Access-Control-Allow-Credentials", "true")
					.build();
        }
    }
	
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/resultoutput")
	@GET
	@Asynchronous
	public void asyncresultrest(@Suspended final AsyncResponse asyncResponse,@QueryParam("key") String key)
	{
		Response result = getResultRegistry(key);
		asyncResponse.resume(result);	
	}
    public Response getResultRegistry(String key){
    	String result;
    	System.out.println(key);
	    try
        {
	
	    int count = DBOperations.getInstance().getCount(key);
	    System.out.println("count value is "+count);
	    if(count >0)
	    {
	    	result = DBOperations.getInstance().getResult(key); 
	    	return Response.status(200).entity(result).build();
	    }
	    else
	    {
	    return Response.status(200).entity("no").build();
	    }
        
        }
        catch(Exception e)
        {
            return Response.status(500).entity(e.getMessage()).build();
        }
        
    }
    @Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/getdata")
    @GET
    
    public Response getdatafromregistry()
    {
    	return Response.status(200).entity("hello processing your request").build();
    }
	

}