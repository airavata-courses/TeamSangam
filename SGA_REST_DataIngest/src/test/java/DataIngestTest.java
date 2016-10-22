
import static org.junit.Assert.*;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.sga.sangam.bean.DataIngestorStatusBean;
import edu.sga.sangam.resources.DataIngestor;



public class DataIngestTest {
	/*
	@Test
	public void checkIncorrectData()
	{
		try
		{
			DataIngestor di = new DataIngestor();
			Response fail = di.getURL("2015","03","4546","KABX", "KABX20150303_001050_V06", "userid", "sessionid", "requestid");
			Assert.assertEquals(400, fail.getStatus());
	}catch(Exception e)
		{
			assertEquals(e.getMessage(),"Registry not started");
		}
	}

	@Test
	public void checkcorrectData() throws Exception {
		
		try
		{
			DataIngestor di = new DataIngestor();
			Response success = di.getURL("2015","03","03","KABX", "KABX20150303_001050_V06", "userid", "sessionid", "requestid");
			if(success.getStatus() ==200)
			{
			DataIngestorStatusBean disb = (DataIngestorStatusBean)success.getEntity();
			Assert.assertEquals("http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/03/KABX/KABX20150303_001050_V06.gz",disb.getUrl());
			}
			else
			{
				String message = (String) success.getEntity();
				assertEquals(message,"Issue with database connections");
			}
		}
		catch(Exception e)
		{
			assertEquals(e.getMessage(),"Registry not started");
		}
	}*/
	
}
