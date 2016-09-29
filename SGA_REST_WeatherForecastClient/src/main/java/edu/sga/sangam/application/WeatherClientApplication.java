package edu.sga.sangam.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import edu.sga.sangam.client.WeatherClient;



@ApplicationPath("sga")
public class WeatherClientApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(WeatherClient.class);
		return classes;
	}
}
