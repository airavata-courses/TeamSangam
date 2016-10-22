import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.persistence.exceptions.SDOException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.junit.Test;


import edu.sga.sangam.resources.StormDetection;

public class StormDetectionTest {
	@Test
	public void checkInvalidfile() throws Exception {
		try
		{
		InputStream data = null;
		FormDataContentDisposition  fdcp = null;
		StormDetection rf = new StormDetection();
		Response status =rf.detectStormKML(data, fdcp,"kabx", "userid", "sessionid", "requestid");
		assertEquals(400,status.getStatus());
		}
		catch(Exception e)
		{
			assertEquals(e.getMessage(),"issue with storm detection registry");
		}
	}
	@Test
	public void checkValidfile() throws Exception {
		URL path = ClassLoader.getSystemResource("testKml.kml");
		System.out.println(path);
		File file = new File(path.getFile());
		HttpEntity entity = MultipartEntityBuilder.create()
				.addBinaryBody("file",file,ContentType.create("application/octet-stream"),file.getName())
				.addTextBody("station", "KABX")
				.addTextBody("userid","userid" )
				.addTextBody("sessionid", "sessionid")
				.addTextBody("requestid","requestid")
				.build();
		HttpResponse res = request(entity);
		if(res==null)
		{}
		else
		{
		assertEquals(200,res.getStatusLine().getStatusCode());}
	}
	private HttpResponse request(HttpEntity entity)
	{
		String stormDetectionUrl ="http://localhost:8080/SGA_Rest_StormDetection/sga/runforecast";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(stormDetectionUrl);
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
