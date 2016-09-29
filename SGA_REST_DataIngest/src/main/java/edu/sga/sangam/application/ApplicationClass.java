package edu.sga.sangam.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import edu.sga.sangam.resources.DataIngestor;

@ApplicationPath("sga")
public class ApplicationClass extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(DataIngestor.class);
		return classes;
	}
}