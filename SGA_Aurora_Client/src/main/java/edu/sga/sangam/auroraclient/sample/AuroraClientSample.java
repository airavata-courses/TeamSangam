package edu.sga.sangam.auroraclient.sample;

import java.text.MessageFormat;
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
import edu.sga.sangam.auroraclient.sdk.ExecutorConfig;
import edu.sga.sangam.auroraclient.sdk.GetJobsResult;
import edu.sga.sangam.auroraclient.sdk.Identity;
import edu.sga.sangam.auroraclient.sdk.JobConfiguration;
import edu.sga.sangam.auroraclient.sdk.JobKey;
import edu.sga.sangam.auroraclient.sdk.ReadOnlyScheduler;
import edu.sga.sangam.auroraclient.sdk.Response;
import edu.sga.sangam.auroraclient.sdk.TaskConfig;
import edu.sga.sangam.auroraclient.util.AuroraThriftClientUtil;
import edu.sga.sangam.auroraclient.util.Constants;
import org.apache.thrift.TException;

/**
 * The Class AuroraClientSample.
 */
public class AuroraClientSample {
	
	/** The aurora scheduler client. */
	private static ReadOnlyScheduler.Client auroraSchedulerClient;
	
	/** The properties. */
	private static Properties properties = new Properties();
	
	/**
	 * Gets the job summary.
	 *
	 * @param client the client
	 * @return the job summary
	 */
	public static void getJobSummary(ReadOnlyScheduler.Client client) {
		try {
			Response response = client.getJobs("team-sangam");
			System.out.println("Response status: " + response.getResponseCode().name());
			if(response.getResult().isSetGetJobsResult()) {
				GetJobsResult result = response.getResult().getGetJobsResult();
				System.out.println(result);
				Set<JobConfiguration> jobConfigs = result.getConfigs();
				for(JobConfiguration jobConfig : jobConfigs) {
					System.out.println(jobConfig);
					JobKey jobKey = jobConfig.getKey();
					Identity owner = jobConfig.getOwner();
					TaskConfig taskConfig = jobConfig.getTaskConfig();
					ExecutorConfig exeConfig = taskConfig.getExecutorConfig();
					
					System.out.println("\n**** JOB CONFIG ****");
						System.out.println("\t # instanceCount: " + jobConfig.getInstanceCount());
						
						System.out.println("\t >> Job Key <<");
							System.out.println("\t\t # name: " + jobKey.getName());
							System.out.println("\t\t # role: " + jobKey.getRole());
							System.out.println("\t\t # environment: " + jobKey.getEnvironment());
							
						System.out.println("\t >> Identity <<");
							System.out.println("\t\t # owner: " + owner.getUser());
							
						System.out.println("\t >> Task Config <<");
							System.out.println("\t\t # numCPUs: " + taskConfig.getNumCpus());
							System.out.println("\t\t # diskMb: " + taskConfig.getDiskMb());
							System.out.println("\t\t # ramMb: " + taskConfig.getRamMb());
							System.out.println("\t\t # priority: " + taskConfig.getPriority());
							
							
						System.out.println("\t >> Executor Config <<");
							System.out.println("\t\t # name: " + exeConfig.getName());
							System.out.println("\t\t # data: " + exeConfig.getData());
				}
				
			}
		} catch (TException e) {
			e.printStackTrace();
		}
	}
	
	public static void createJob() throws Exception {
		JobKeyBean jobKey = new JobKeyBean("devel", "team-sangam", "sangam_test_job_1");
		IdentityBean owner = new IdentityBean("team-sangam");
		
		/*ProcessBean proc1 = new ProcessBean("process_1", "docker run "
												+ "-it --volumes-from wpsgeog "
												+ "--volumes-from wrfinputsandy "
												+ "-v ~/wrfoutput:/wrfoutput "
												+ "--name sangam-ncarwrfsandy-" + "02"
												+ " bigwxwrf/ncar-wrf /wrf/run-wrf", false);
		ProcessBean proc2 = new ProcessBean("process_2", "docker run -it "
														+ "--rm=true "
														+ "-v ~/wrfoutput:/wrfoutput "
														+ "--name sangam-postproc-" + "02" 
														+ " bigwxwrf/ncar-ncl", false);*/
		
		ProcessBean proc1 = new ProcessBean("process_11","docker run -i --volumes-from wpsgeog --volumes-from wrfinputsandy -v ~/wrfoutput:/wrfoutput --name sangam-ncarwrfsandy-03 bigwxwrf/ncar-wrf /wrf/run-wrf",false);
		ProcessBean proc2 = new ProcessBean("process_12", "docker run -i --rm=true -v ~/wrfoutput:/wrfoutput --name sangam-postproc-03 bigwxwrf/ncar-ncl",false);
		//ProcessBean proc1 = new ProcessBean("process_1", "echo 'hello_world_1'", false);
		//ProcessBean proc2 = new ProcessBean("process_2", "echo 'hello_world_2'", false);
		Set<ProcessBean> processes = new HashSet<>();
		processes.add(proc1);
		processes.add(proc2);
		
		ResourceBean resources = new ResourceBean(0.3, 200, 100);
		
		TaskConfigBean taskConfig = new TaskConfigBean("sangam_run_forecast", processes, resources);
		JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");
		
		String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
		System.out.println(executorConfigJson);
		
		ThriftClient client = ThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.createJob(jobConfig);
		System.out.println(response);
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		 try {
			properties.load(AuroraClientSample.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
			String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
			String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
			auroraSchedulerClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));
			
			// get jobs summary
//			
			AuroraClientSample.getJobSummary(auroraSchedulerClient);
			
			// create sample job
//			
			AuroraClientSample.createJob();
			ThriftClient client = ThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
			ResponseBean response = client.getPendingReasonForJob(new JobKeyBean("devel", "team-sangam", "hello_pending"));
			System.out.println(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}

}
