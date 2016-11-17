package edu.sga.sangam.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetStatsBean {
    String userid;
    String sessionid;
    String requestid;
    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }
    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
    /**
     * @return the sessionid
     */
    public String getSessionid() {
        return sessionid;
    }
    /**
     * @param sessionid the sessionid to set
     */
    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
    /**
     * @return the requestid
     */
    public String getRequestid() {
        return requestid;
    }
    /**
     * @param requestid the requestid to set
     */
    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }
    
    
    
}
