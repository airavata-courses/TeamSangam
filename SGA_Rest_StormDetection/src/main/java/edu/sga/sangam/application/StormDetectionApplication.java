package edu.sga.sangam.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import edu.sga.sangam.resources.StormDetection;


@ApplicationPath("sga")
public class StormDetectionApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(StormDetection.class);
		classes.add(MultiPartFeature.class);
		return classes;
	}
}
