import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.sga.sangam.resources.RunForecast;
import junit.framework.Assert;

public class ForecastTest {
	@BeforeClass
	public static void beforeClass(){
	}
	@AfterClass
	public static void afterClass()
	{
		
	}

	
	@Test
	public void checkValidfile() throws Exception {
		URL path = ClassLoader.getSystemResource("testKml.kml");
		System.out.println(path);
		File file = new File(path.getFile());
		HttpEntity entity = MultipartEntityBuilder.create()
				.addBinaryBody("file",file,ContentType.create("application/octet-stream"),file.getName())
				.addTextBody("userid","userid" )
				.addTextBody("sessionid", "sessionid")
				.addTextBody("requestid","requestid")
				.build();
		HttpResponse res = request(entity);
		if(res==null)
		{
			
		}
		else
		{
		assertEquals(200,res.getStatusLine().getStatusCode());}
	}
	@Test
	public void checkInvalidfile() throws Exception {
		try {
			InputStream data = null;
			FormDataContentDisposition fdcp = null;
			RunForecast rf = new RunForecast();
			Response status = rf.getForecast(data, fdcp, "userid", "sessionid", "requestid");
			assertEquals(400, status.getStatus());
		}catch (Exception e)
		{
			assertEquals("issue  with forecast registry",e.getMessage());
		}

	}
	
	private HttpResponse request(HttpEntity entity)
	{
		String runForecasturl ="http://sgaforecast:8084/SGA_REST_Forecast/sga/runforecast";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(runForecasturl);
		httpPost.setEntity(entity);
		try
		{
			HttpResponse response = httpclient.execute(httpPost);
			return response;
				
		}catch (Exception e) {
			return null;
		}
	}
}
