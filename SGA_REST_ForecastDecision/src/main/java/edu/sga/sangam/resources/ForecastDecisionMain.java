package edu.sga.sangam.resources;

import java.io.IOException;

public class ForecastDecisionMain {
	public static void main(String[] args) throws IOException {
		 String topic = "forecast";
		    // Start group of User Consumer Thread
		    ForecastConsumer consumerThread = new ForecastConsumer(topic);
		    Thread t2 = new Thread(consumerThread);
		    t2.start();
	 }
	}


