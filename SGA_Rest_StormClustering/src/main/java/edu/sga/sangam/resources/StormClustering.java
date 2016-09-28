package edu.sga.sangam.resources;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;

import org.apache.commons.io.FilenameUtils;


import edu.sga.sangam.services.StormClusteringService;;


@Path("stormclustering")
public class StormClustering {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response returnKMLFile(@FormParam("url") String url )
	{
		System.out.println(url);
		try
		{
			String fileName = FilenameUtils.getBaseName(url);
			StormClusteringService  stormClusteringService = new StormClusteringService();
			File kmlfile = stormClusteringService.generateKmlFile(fileName);
			//StormDetectionResponseBean sdrs = new StormDetectionResponseBean(kmlfile);
			return Response.ok(kmlfile).header("Content-Disposition", "attachment; filename=\"" +fileName+".kml"+ "\"").
					build();
		}catch(Exception e)
		{
			return Response.status(400).entity("Error in Generating kml file while running storm detection algorithm").build();
		}
	}
}
