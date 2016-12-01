package edu.sga.sangam.resources;

import java.io.IOException;

public class StormClusteringMain {

	public static void main(String[] args) throws IOException {
		String topic = "stormclustering";
	    // Start group of User Consumer Thread
	    StormClusteringConsumer consumerThread = new StormClusteringConsumer(topic);
	    Thread t2 = new Thread(consumerThread);
	    t2.start();
	}
	}

