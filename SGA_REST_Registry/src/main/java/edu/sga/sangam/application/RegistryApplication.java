package edu.sga.sangam.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import edu.sga.sangam.resources.Registry;


@ApplicationPath("sga")
public class RegistryApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(Registry.class);
		return classes;
	}

}
