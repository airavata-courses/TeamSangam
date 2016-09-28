package edu.sga.sangam.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import edu.sga.sangam.resources.StormClustering;;

@ApplicationPath("sga")
public class StormClusteringApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(StormClustering.class);
		classes.add(MultiPartFeature.class);
		return classes;
	}
}
