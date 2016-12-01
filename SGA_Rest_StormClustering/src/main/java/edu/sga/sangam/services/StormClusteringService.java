package edu.sga.sangam.services;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;


public class StormClusteringService {
	File tmpfile= null;
	public File generateKmlFile(String fileName) throws IOException {
		try
		{
		tmpfile = File.createTempFile(fileName, ".kml");
		Kml kml = new Kml();
		Random ran = new Random();
		int x = ran.nextInt(6) + 5;
		Document document = kml.createAndSetDocument();
		for(int i=0;i<x;i++){
			Coordinates c = new Coordinates();
		document.createAndAddPlacemark().withName(fileName).withOpen(Boolean.TRUE)  
	    .createAndSetPoint().addToCoordinates(c.getX(), c.getY()); 
		
		}
		kml.marshal(tmpfile);
		return tmpfile;
		}catch(Exception e)
		{
			System.out.println("hello error occured here");
			throw new IOException("Error in Generating kml file while running storm detection algorithm");
		}
		finally
		{
			tmpfile.deleteOnExit();
		}
	}

}


class Coordinates
{
	private double x;
	private double y;
	double latmin =30;
	double latmax =48;
	double longmax = -75;
	double longmin =-125;
	public Coordinates()
	{
		this.x = latmin+(Math.random()*(latmax-latmin));
		this.y = longmax+(Math.random()*(longmin-longmax));
	}
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
		
}
	