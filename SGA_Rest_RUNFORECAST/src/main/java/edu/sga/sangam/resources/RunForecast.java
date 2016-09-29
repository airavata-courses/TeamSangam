package edu.sga.sangam.resources;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.amazonaws.util.IOUtils;

import edu.sga.sangam.services.RunForecastService;
@Path("runforecast")
public class RunForecast {
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getForecast(@FormDataParam("file") InputStream input,@FormDataParam("file") FormDataContentDisposition fileDetail)
	{
		if (input == null || fileDetail == null )
			return Response.status(400).entity("File is empty").build();
		RunForecastService runForecastService = new RunForecastService();
		try{
			System.out.println("running forecast");
			String fileName = fileDetail.getFileName();
			byte[] bytes = IOUtils.toByteArray(input);
			String jsonString =runForecastService.getForecastDetails(fileName,bytes);
			return Response.status(200).entity(jsonString).build();
		
	}catch(Exception e)
		{
		return Response.status(500).entity("Error in generating coordinates").build();
		}
	}

}
