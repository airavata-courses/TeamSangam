package edu.sga.sangam.resources;

import java.io.IOException;

public class DataIngestorMain {
	 public static void main(String[] args) throws IOException {
		    String topic = "dataingestor";
		    // Start group of Data Ingestor Consumer Thread
		    DataIngestorConsumer consumerThread = new DataIngestorConsumer(topic);
		    Thread t2 = new Thread(consumerThread);
		    t2.start();
	 }

}
