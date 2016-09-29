package edu.sga.sangam.resources;

import java.io.File;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sga.sangam.bean.StormDetectionResponseBean;
import edu.sga.sangam.services.StormDetectionService;



@Path("stormdetection")
public class StormDetection {
	private final Logger log = LoggerFactory.getLogger(StormDetection.class);
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response detectStormKML(@FormDataParam("file") InputStream input,@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("station") String station)
	{
		
		if (input == null || fileDetail == null )
			return Response.status(400).entity("File is empty").build();
		StormDetectionService stormDetectionService = new StormDetectionService();
		try
		{
			String fileName = fileDetail.getFileName();
			File kmlfile = stormDetectionService.generateKMLFile(fileName,station);
			return Response.ok(kmlfile).header("Content-Disposition", "attachment; filename=\"" +fileName+"_cluster.kml"+ "\"").
					build();
			
		}
		catch(Exception e)
		{
			return Response.status(500).entity("Error in generating Storm clustering file").build();
		}
		
	}
}
