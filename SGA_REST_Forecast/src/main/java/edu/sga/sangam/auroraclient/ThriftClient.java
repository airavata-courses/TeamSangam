/*************************************************
 * Title: 		Aurora-client
 * Author: 		Gourav Shenoy
 * Date : 		12/9/2016
 * Code Version: 0.0.17
 * Original Code Location: https://github.com/gouravshenoy/airavata/tree/aurora-thrift-client/modules/cloud/aurora-client/
 */


package edu.sga.sangam.auroraclient;


import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.sga.sangam.auroraclient.bean.*;
import edu.sga.sangam.auroraclient.sample.AuroraClientSample;
import edu.sga.sangam.auroraclient.sdk.AuroraSchedulerManager;
import edu.sga.sangam.auroraclient.sdk.JobConfiguration;
import edu.sga.sangam.auroraclient.sdk.JobKey;
import edu.sga.sangam.auroraclient.sdk.ReadOnlyScheduler;
import edu.sga.sangam.auroraclient.sdk.Response;
import edu.sga.sangam.auroraclient.sdk.TaskQuery;
import edu.sga.sangam.auroraclient.util.AuroraThriftClientUtil;
import edu.sga.sangam.auroraclient.util.Constants;
import edu.sga.sangam.auroraclient.util.ResponseResultType;

import org.apache.thrift.TException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThriftClient {
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ThriftClient.class);
	
	/** The properties. */
	private static Properties properties = new Properties();
	
	/** The read only scheduler client. */
	private ReadOnlyScheduler.Client readOnlySchedulerClient = null;
	
	/** The aurora scheduler manager client. */
	private AuroraSchedulerManager.Client auroraSchedulerManagerClient = null;
	
	public ReadOnlyScheduler.Client getReadOnlySchedulerClient() {
		return readOnlySchedulerClient;
	}

	public void setReadOnlySchedulerClient(
			ReadOnlyScheduler.Client readOnlySchedulerClient) {
		this.readOnlySchedulerClient = readOnlySchedulerClient;
	}

	public AuroraSchedulerManager.Client getAuroraSchedulerManagerClient() {
		return auroraSchedulerManagerClient;
	}

	public void setAuroraSchedulerManagerClient(
			AuroraSchedulerManager.Client auroraSchedulerManagerClient) {
		this.auroraSchedulerManagerClient = auroraSchedulerManagerClient;
	}

	
	/** The thrift client. */
	private static ThriftClient thriftClient = null;
	
	/**
	 * Instantiates a new aurora thrift client.
	 */
	private ThriftClient() {}
	
	/**
	 * Gets the aurora thrift client.
	 *
	 * @param auroraSchedulerPropFile the aurora scheduler prop file
	 * @return the aurora thrift client
	 * @throws Exception the exception
	 */
	public static ThriftClient getAuroraThriftClient(String auroraSchedulerPropFile) throws Exception {
		try {
			if(thriftClient == null) {
				thriftClient = new ThriftClient();
				
				// construct connection url for scheduler
				properties.load(AuroraClientSample.class.getClassLoader().getResourceAsStream(auroraSchedulerPropFile));
				String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
				String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
				String connectionUrl = MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort);
				
				thriftClient.readOnlySchedulerClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(connectionUrl);
				thriftClient.auroraSchedulerManagerClient = AuroraSchedulerClientFactory.createSchedulerManagerClient(connectionUrl);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return thriftClient;
	}
	
	/**
	 * Creates the job.
	 *
	 * @param jobConfigBean the job config bean
	 * @return the response bean
	 * @throws Exception the exception
	 */
	public ResponseBean createJob(JobConfigBean jobConfigBean) throws Exception {
		ResponseBean response = null;
		try {
			if(jobConfigBean != null) {
				JobConfiguration jobConfig = AuroraThriftClientUtil.getAuroraJobConfig(jobConfigBean);
				Response createJobResponse = this.auroraSchedulerManagerClient.createJob(jobConfig);
				response = AuroraThriftClientUtil.getResponseBean(createJobResponse, ResponseResultType.CREATE_JOB);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	/**
	 * Kill tasks.
	 *
	 * @param jobKeyBean the job key bean
	 * @param instances the instances
	 * @return the response bean
	 * @throws Exception the exception
	 */
	public ResponseBean killTasks(JobKeyBean jobKeyBean, Set<Integer> instances) throws Exception {
		ResponseBean response = null;
		try {
			if(jobKeyBean != null) {
				JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
				Response killTaskResponse = this.auroraSchedulerManagerClient.killTasks(jobKey, instances);
				response = AuroraThriftClientUtil.getResponseBean(killTaskResponse, ResponseResultType.KILL_TASKS);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	/**
	 * Gets the job list.
	 *
	 * @param ownerRole the owner role
	 * @return the job list
	 * @throws Exception the exception
	 */
	public GetJobsResponseBean getJobList(String ownerRole) throws Exception {
		GetJobsResponseBean response = null;
		try {
				Response jobListResponse = this.readOnlySchedulerClient.getJobs(ownerRole);
				response = (GetJobsResponseBean) AuroraThriftClientUtil.getResponseBean(jobListResponse, ResponseResultType.GET_JOBS);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	/**
	 * Gets the pending reason for job.
	 *
	 * @param jobKeyBean the job key bean
	 * @return the pending reason for job
	 * @throws Exception the exception
	 */
	public PendingJobReasonBean getPendingReasonForJob(JobKeyBean jobKeyBean) throws Exception {
		PendingJobReasonBean response = null;
		try {
				JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
				Set<JobKey> jobKeySet = new HashSet<>();
				jobKeySet.add(jobKey);
				
				TaskQuery query = new TaskQuery();
				query.setJobKeys(jobKeySet);
				
				Response pendingReasonResponse = this.readOnlySchedulerClient.getPendingReason(query);
				response = (PendingJobReasonBean) AuroraThriftClientUtil.getResponseBean(pendingReasonResponse, ResponseResultType.GET_PENDING_JOB_REASON);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}
	
	/**
	 * Gets the job details.
	 *
	 * @param jobKeyBean the job key bean
	 * @return the job details
	 * @throws Exception the exception
	 */
	public ResponseBean getJobDetails(JobKeyBean jobKeyBean) throws Exception {
		JobDetailsResponseBean response = null;
		try {
			if(jobKeyBean != null) {
				JobKey jobKey = AuroraThriftClientUtil.getAuroraJobKey(jobKeyBean);
				Set<JobKey> jobKeySet = new HashSet<>();
				jobKeySet.add(jobKey);
				
				TaskQuery query = new TaskQuery();
				query.setJobKeys(jobKeySet);
				
				Response jobDetailsResponse = this.readOnlySchedulerClient.getTasksStatus(query);
				response = (JobDetailsResponseBean) AuroraThriftClientUtil.getResponseBean(jobDetailsResponse, ResponseResultType.GET_JOB_DETAILS);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		return response;
	}	
	
}
