package edu.sga.sangam.resources;

import java.io.IOException;

public class RunForecastMain {
	public static void main(String[] args) throws IOException {
	   String topic = "runforecast";
	    // Start group of User Consumer Thread
	    RunForecastConsumer consumerThread = new RunForecastConsumer(topic);
	    Thread t2 = new Thread(consumerThread);
	    t2.start();
	}

}
