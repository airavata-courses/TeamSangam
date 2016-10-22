import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.sga.sangam.resources.ForecastDecision;

public class ForecastDecisionTest {
	@BeforeClass
	public static void beforeClass(){
	}
	@AfterClass
	public static void afterClass()
	{
		
	}

	@Test
    public void checkStormExists() throws Exception {
		try
		{
		ForecastDecision fd = new ForecastDecision();
		String res =fd.getDecision("rama","reddy","abc");
		Assert.assertTrue(res.equals("yes")||res.equals("no"));
		}catch(Exception e)
		{
			assertEquals(e.getMessage(),"issue  with forecast decision registry");
		}
		
		
	}
	
	
	
	}
	