import static org.junit.Assert.*;


import java.sql.Timestamp;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.junit.Test;

import edu.sga.sangam.resources.DataIngestRequest;
import edu.sga.sangam.resources.ForecastDecisionBean;
import edu.sga.sangam.resources.Registry;
import edu.sga.sangam.resources.RunForecastBean;
import edu.sga.sangam.resources.StormClusterBean;
import edu.sga.sangam.resources.StormDetectionBean;

public class RegistryTest {
	@Test
	public void testdataingestor()
	{
		try
		{
		DataIngestRequest di = new DataIngestRequest();
		di.setUserid("userid");
		di.setSessionid("sessionid");
		di.setRequestid("requestid");
		di.setRequestData("requestData");
		di.setResponseData("responseData");
		Registry register = new Registry();
		Response rs = register.dataIngestorRequest(di);
		if(200 ==rs.getStatus()){
			assertEquals("userid",(String) rs.getEntity());
		}
		else
		{
			assertEquals(500,rs.getStatus());
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void teststormclusteringingestor()
	{
		try
		{

		StormClusterBean di = new StormClusterBean();
		di.setUserid("userid");
		di.setSessionid("sessionid");
		di.setRequestid("requestid");
		di.setRequestData("requestData");
		di.setResponseData("responseData");
		
		Registry register = new Registry();
		Response rs = register.StormCluster(di);
		if(200 ==rs.getStatus()){
			assertEquals("userid",(String) rs.getEntity());
		}
		else
		{
			assertEquals(500,rs.getStatus());
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void teststormDataingestor()
	{
		try
		{
		StormDetectionBean di = new StormDetectionBean();
		di.setUserid("userid");
		di.setSessionid("sessionid");
		di.setRequestid("requestid");
		di.setRequestData("requestData");
		di.setResponseData("responseData");
		Registry register = new Registry();
		Response rs = register.StormDetection(di);
		if(200 ==rs.getStatus()){
			assertEquals("userid",(String) rs.getEntity());
		}
		else
		{
			assertEquals(500,rs.getStatus());
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testforecastdecision()
	{
		try
		{
		ForecastDecisionBean di = new ForecastDecisionBean();
		di.setUserid("userid");
		di.setSessionid("sessionid");
		di.setRequestid("requestid");
		di.setRequestData("requestData");
		di.setResponseData("responseData");
		Registry register = new Registry();
		Response rs = register.ForecastDecision(di);
		if(200 ==rs.getStatus()){
			assertEquals("userid",(String) rs.getEntity());
		}
		else
		{
			assertEquals(500,rs.getStatus());
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void runforecast()
	{
		try
		{
		RunForecastBean di = new RunForecastBean();
		di.setUserid("userid");
		di.setSessionid("sessionid");
		di.setRequestid("requestid");
		di.setRequestData("requestData");
		di.setResponseData("responseData");
		Registry register = new Registry();
		Response rs = register.RunForecast(di);
		if(200 ==rs.getStatus()){
			assertEquals("userid",(String) rs.getEntity());
		}
		else
		{
			assertEquals(500,rs.getStatus());
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}
