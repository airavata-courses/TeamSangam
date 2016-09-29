package edu.sga.sangam.resources;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("forecastdecision")
public class ForecastDecision {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDecision()
	{
		Random r = new Random();
		int decision = r.nextInt();
		if(decision<0.25)
		{
			return "no";
		}
		else
		{
			return "yes";
		}
	}
	

}
