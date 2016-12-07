package edu.sga.sangam.services;

import java.io.IOException;

public class ResultMain {
	public static void main(String[] args) throws IOException {
		
	 String topic = "result";
	    // Start group of User Consumer Thread
	    ResultConsumer consumerThread = new ResultConsumer(topic);
	    Thread t2 = new Thread(consumerThread);
	    t2.start();
}
}
