package edu.sga.sangam.client;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Asynchronous;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import edu.sga.sangam.auroraclient.ThriftClient;
import edu.sga.sangam.auroraclient.bean.JobDetailsResponseBean;
import edu.sga.sangam.auroraclient.sdk.JobKey;
import edu.sga.sangam.auroraclient.sdk.ScheduledTask;
import edu.sga.sangam.auroraclient.sdk.TaskQuery;
import edu.sga.sangam.auroraclient.util.AuroraThriftClientUtil;
import edu.sga.sangam.auroraclient.util.Constants;
import edu.sga.sangam.auroraclient.util.ResponseResultType;
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
	

	@GET
	@Path("jobstatus")
	@Asynchronous
	public void asyncjobstatusrest(@Suspended final AsyncResponse asyncResponse,@QueryParam("jobid") String jobid)
	{
		System.out.println("inside async call");
		Response result = getJobStatus(jobid);
		asyncResponse.resume(result);	
	}
	public Response getJobStatus(String jobid)
	{
		JobResponseStatus jrs = new JobResponseStatus();
		try
		{

			ThriftClient client = ThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
			JobKey jobKey  = new JobKey( "team-sangam", "devel",jobid);
			Set<JobKey> jobKeySet = new HashSet<JobKey>();
			jobKeySet.add(jobKey);
			TaskQuery query = new TaskQuery();
			query.setJobKeys(jobKeySet);
			edu.sga.sangam.auroraclient.sdk.Response jobDetailsResponse =client.getReadOnlySchedulerClient().getTasksStatus(query);
			JobDetailsResponseBean detailsReponse =(JobDetailsResponseBean) AuroraThriftClientUtil.getResponseBean(jobDetailsResponse, ResponseResultType.GET_JOB_DETAILS);
			String host="";
			for(ScheduledTask s : detailsReponse.getTasks())
				{
				host=s.assignedTask.slaveHost;
					if(host.equals("sga-mesos-slave-1"))
					{
						host="52.53.179.0";
					}
					else if(host.equals("sga-mesos-slave-2"))
					{
						host="54.215.219.32";
					}
					jrs.setJobName(s.getAssignedTask().getTaskId());
					jrs.setHostName(host);
					jrs.setJobStatus(s.getStatus().toString());
					System.out.println(s.getAssignedTask().getTaskId());
					System.out.println("task status"+s.getStatus().toString());
					System.out.println("host is "+host);
				} 
			
			return Response.status(200).entity(jrs).build();
		}
			
			
		catch(Exception e)
		{
			return Response.status(400).entity(e.getMessage()).build();
		}
	}
}
