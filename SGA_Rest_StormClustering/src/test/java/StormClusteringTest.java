import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import edu.sga.sangam.resources.StormClustering;

public class StormClusteringTest {
	@Test
	public void checkCorrectURL() throws Exception {
		//"http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/03/KABX/KABX20150303_001050_V06.gz
		StormClustering sc = new StormClustering();
		try
		{
		String url = "http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/03/KABX/KABX20150303_001050_V06.gz";
		Response fail = sc.returnKMLFile(url, "userid", "sessionid", "requestid");
		assertEquals(200,fail.getStatus());
		}
		catch(Exception e)
		{
			System.out.println("inside Exception");
			Assert.assertEquals(e.getMessage(), "issue  with storm cluster registry");
		}
	}
	@Test
	public void checkWrongUrl() throws Exception {
		//"http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/03/KABX/KABX20150303_001050_V06.gz
		StormClustering sc = new StormClustering();
		try
		{
		String url = "http://noaa-nexrad-level2.s3.amazonaws.com/2015/03/KABX/KABX20150303_001050_V06.gz";
		Response fail = sc.returnKMLFile(url, "userid", "sessionid", "requestid");
		assertEquals(400,fail.getStatus());
		}
		catch(Exception e)
		{
			System.out.println("inside Exception");
			Assert.assertEquals(e.getMessage(), "issue  with storm cluster registry");
		}
	}

}
