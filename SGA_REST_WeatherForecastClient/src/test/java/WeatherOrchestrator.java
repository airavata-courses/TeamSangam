import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Test;

import edu.sga.sangam.client.WeatherClient;

public class WeatherOrchestrator {
	@Test
	public void checkdata() throws Exception {
		WeatherClient wc = new WeatherClient();
		try
		{
		Response fail = wc.getURL("1993","11","10","KABX", "dafaafa", "userid", "sessionid", "requestid");
		assertEquals(200,fail.getStatus());
		}
		catch(Exception e)
		{
		 
		}
		
	}
	
}
