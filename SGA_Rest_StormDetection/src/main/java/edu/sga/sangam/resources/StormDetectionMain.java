package edu.sga.sangam.resources;

import java.io.IOException;

public class StormDetectionMain {
	public static void main(String[] args) throws IOException {
		String topic = "stormdetection";
	    // Start group of User Consumer Thread
	    StormDetectionConsumer consumerThread = new StormDetectionConsumer(topic);
	    Thread t2 = new Thread(consumerThread);
	    t2.start();
	}
}
