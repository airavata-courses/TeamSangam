/*************************************************
 * Title: 		Aurora-client
 * Author: 		Gourav Shenoy
 * Date : 		12/9/2016
 * Code Version: 0.0.17
 * Original Code Location: https://github.com/gouravshenoy/airavata/tree/aurora-thrift-client/modules/cloud/aurora-client/
 */


package edu.sga.sangam.auroraclient.bean;

public class JobKeyBean {

	/** The environment. */
	private String environment;
	
	/** The role. */
	private String role;
	
	/** The name. */
	private String name;
	
	/**
	 * Instantiates a new job key bean.
	 *
	 * @param environment the environment
	 * @param role the role
	 * @param name the name
	 */
	public JobKeyBean(String environment, String role, String name) {
		this.environment = environment;
		this.role = role;
		this.name = name;
	}
	
	/**
	 * Gets the environment.
	 *
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}
	
	/**
	 * Sets the environment.
	 *
	 * @param environment the new environment
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}