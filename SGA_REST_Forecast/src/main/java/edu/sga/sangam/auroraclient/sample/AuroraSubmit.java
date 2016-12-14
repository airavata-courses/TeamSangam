package edu.sga.sangam.auroraclient.sample;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.sga.sangam.auroraclient.AuroraSchedulerClientFactory;
import edu.sga.sangam.auroraclient.ThriftClient;
import edu.sga.sangam.auroraclient.bean.IdentityBean;
import edu.sga.sangam.auroraclient.bean.JobConfigBean;
import edu.sga.sangam.auroraclient.bean.JobKeyBean;
import edu.sga.sangam.auroraclient.bean.ProcessBean;
import edu.sga.sangam.auroraclient.bean.ResourceBean;
import edu.sga.sangam.auroraclient.bean.ResponseBean;
import edu.sga.sangam.auroraclient.bean.TaskConfigBean;
import edu.sga.sangam.auroraclient.sdk.Identity;
import edu.sga.sangam.auroraclient.sdk.JobKey;
import edu.sga.sangam.auroraclient.sdk.ReadOnlyScheduler;
import edu.sga.sangam.auroraclient.sdk.Resource;
import edu.sga.sangam.auroraclient.sdk.TaskConfig;
import edu.sga.sangam.auroraclient.util.AuroraThriftClientUtil;
import edu.sga.sangam.auroraclient.util.Constants;

public class AuroraSubmit {
	
	public  void createJob(String jobid,String datetime) throws Exception {
		JobKeyBean jobKey = new JobKeyBean("devel", "team-sangam", jobid);
		IdentityBean owner = new IdentityBean("team-sangam");
		
		
		ProcessBean proc1 = new ProcessBean("process_1", "docker run -i --volumes-from wpsgeog --volumes-from wrfinputsandy -v ~/wrfoutput:/wrfoutput --name "+ datetime + " bigwxwrf/ncar-wrf /wrf/run-wrf", false);
		ProcessBean proc2 = new ProcessBean("process_2", "docker run -i --rm=true -v ~/wrfoutput:/wrfoutput --name postproc bigwxwrf/ncar-ncl", false);
		
		ResourceBean resources = new ResourceBean(0.3, 500, 200);
		Set<ProcessBean> processes = new HashSet<>();
		processes.add(proc1);
		processes.add(proc2);
		
		
		TaskConfigBean taskConfig = new TaskConfigBean("sangam_run_forecast", processes, resources);
		JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");
		
		String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
		System.out.println("execugof"+executorConfigJson);
		
		ThriftClient client = ThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.createJob(jobConfig);
		System.out.println("response above response"+response);
	}
	
	
	public String startJob() throws Exception{
		
		try {
			final ReadOnlyScheduler.Client auroraSchedulerClient;
			Properties properties = new Properties();
			
			properties.load(AuroraSubmit.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
			String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
			//System.out.println(auroraHost);
			String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
			auroraSchedulerClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));
			//AuroraJobSubmit.getJobSummary(auroraSchedulerClient);
			
			Date dNow = new Date();
	        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
	        String datetime = ft.format(dNow);
	        System.out.println(datetime);
	        String jobid = "sangam_"+datetime;
	        createJob(jobid,datetime);
	        return jobid;
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
			
	}
}
