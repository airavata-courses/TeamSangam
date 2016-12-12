/*************************************************
 * Title: 		Aurora-client
 * Author: 		Gourav Shenoy
 * Date : 		12/9/2016
 * Code Version: 0.0.17
 * Original Code Location: https://github.com/gouravshenoy/airavata/tree/aurora-thrift-client/modules/cloud/aurora-client/
 */


package edu.sga.sangam.auroraclient.bean;

import edu.sga.sangam.auroraclient.util.ResponseCodeEnum;

public class ResponseBean {

		/** The response code. */
		private ResponseCodeEnum responseCode;
		
		/** The server info. */
		private ServerInfoBean serverInfo;

		/**
		 * Gets the response code.
		 *
		 * @return the response code
		 */
		public ResponseCodeEnum getResponseCode() {
			return responseCode;
		}

		/**
		 * Sets the response code.
		 *
		 * @param responseCode the new response code
		 */
		public void setResponseCode(ResponseCodeEnum responseCode) {
			this.responseCode = responseCode;
		}

		/**
		 * Gets the server info.
		 *
		 * @return the server info
		 */
		public ServerInfoBean getServerInfo() {
			return serverInfo;
		}

		/**
		 * Sets the server info.
		 *
		 * @param serverInfo the new server info
		 */
		public void setServerInfo(ServerInfoBean serverInfo) {
			this.serverInfo = serverInfo;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ResponseBean [responseCode=" + responseCode + ", serverInfo=" + serverInfo + "]";
		}
	
}
