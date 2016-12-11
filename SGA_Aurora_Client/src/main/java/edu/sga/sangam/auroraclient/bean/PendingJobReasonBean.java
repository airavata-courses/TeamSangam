/*************************************************
 * Title: 		Aurora-client
 * Author: 		Gourav Shenoy
 * Date : 		12/9/2016
 * Code Version: 0.0.17
 * Original Code Location: https://github.com/gouravshenoy/airavata/tree/aurora-thrift-client/modules/cloud/aurora-client/
 */


package edu.sga.sangam.auroraclient.bean;

import java.util.Set;

import edu.sga.sangam.auroraclient.sdk.PendingReason;

public class PendingJobReasonBean extends ResponseBean {

	/** The reasons. */
	private Set<PendingReason> reasons;
	
	/**
	 * Instantiates a new pending job response bean.
	 *
	 * @param responseBean the response bean
	 */
	public PendingJobReasonBean(ResponseBean responseBean) {
		this.setResponseCode(responseBean.getResponseCode());
		this.setServerInfo(responseBean.getServerInfo());
	}

	/**
	 * Gets the reasons.
	 *
	 * @return the reasons
	 */
	public Set<PendingReason> getReasons() {
		return reasons;
	}

	/**
	 * Sets the reasons.
	 *
	 * @param reasons the new reasons
	 */
	public void setReasons(Set<PendingReason> reasons) {
		this.reasons = reasons;
	}

	/* (non-Javadoc)
	 * @see org.apache.airavata.cloud.aurora.client.bean.ResponseBean#toString()
	 */
	@Override
	public String toString() {
		return "PendingJobResponseBean [reasons=" + reasons + "]";
	}
	
}
