package edu.sga.sangam.auroraclient.sample;

import java.util.HashSet;
import java.util.Set;

import edu.sga.sangam.auroraclient.ThriftClient;
import edu.sga.sangam.auroraclient.bean.JobDetailsResponseBean;
import edu.sga.sangam.auroraclient.sdk.JobKey;
import edu.sga.sangam.auroraclient.sdk.Response;
import edu.sga.sangam.auroraclient.sdk.ScheduledTask;
import edu.sga.sangam.auroraclient.sdk.TaskQuery;
import edu.sga.sangam.auroraclient.util.AuroraThriftClientUtil;
import edu.sga.sangam.auroraclient.util.Constants;
import edu.sga.sangam.auroraclient.util.ResponseResultType;

public class AuroraClientStatus {
	public static void main(String[] args) throws Exception {
		
		ThriftClient client = ThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		JobKey jobKey  = new JobKey( "team-sangam", "devel","sangam_test_job_1");
		Set<JobKey> jobKeySet = new HashSet<JobKey>();
		jobKeySet.add(jobKey);
		TaskQuery query = new TaskQuery();
		query.setJobKeys(jobKeySet);
		Response jobDetailsResponse =client.getReadOnlySchedulerClient().getTasksStatus(query);
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
				
				System.out.println(s.getAssignedTask().getTaskId());
				System.out.println("task status"+s.getStatus().toString());
				System.out.println("host is "+host);
			}
	}
}
