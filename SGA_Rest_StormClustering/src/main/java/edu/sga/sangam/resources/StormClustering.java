package edu.sga.sangam.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;

import edu.sga.sangam.services.StormClusteringService;;


@Path("stormclustering")
public class StormClustering {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response returnKMLFile(@FormParam("url") String url,@QueryParam("userid") String userid,
			@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid ) throws Exception
	{
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		JSONObject stormcluster = new JSONObject();
		stormcluster.put("userid", userid);
		stormcluster.put("sessionid",sessionid);
		stormcluster.put("requestid",requestid);
		stormcluster.put("requestData", "Requested Data for the url "+url);
		stormcluster.put("requestTime", df2.format(date));
		
		
		try
		{
			String fileName = FilenameUtils.getBaseName(url);
			StormClusteringService  stormClusteringService = new StormClusteringService();
			File kmlfile = stormClusteringService.generateKmlFile(fileName);
			//StormDetectionResponseBean sdrs = new StormDetectionResponseBean(kmlfile);
			stormcluster.put("responseData", "Response returned is a file with file name "+fileName+".kml");
			stormcluster.put("responseTime", df2.format(date));
			registry(stormcluster);
			return Response.ok(kmlfile).header("Content-Disposition", "attachment; filename=\"" +fileName+".kml"+ "\"").
					build();
		}catch(Exception e)
		{
			stormcluster.put("responseData", "Response returned is Error in Generating kml file while running storm detection algorithm");
			stormcluster.put("responseTime", df2.format(date));
			registry(stormcluster);
			return Response.status(400).entity("Error in Generating kml file while running storm detection algorithm").build();
		}
	}
	public void registry(JSONObject requestDataIngestor) throws Exception
	{
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://54.209.48.186:8085/SGA_REST_Registry/sga/resgitry/sclogdata");
		StringRequestEntity entity = new StringRequestEntity(requestDataIngestor.toJSONString(),"application/json","UTF-8");
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		try
		{
		int statusCode = client.executeMethod(post);
		}
		catch(Exception e)
		{
			throw new Exception("issue  with storm cluster registry");
		}
		
		
	}
}
