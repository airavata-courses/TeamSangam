package edu.sga.sangam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import com.google.common.io.Resources;

public class ForecastConsumer implements Runnable {
	final static Logger logger = Logger.getLogger(ForecastConsumer.class);
	 private final KafkaConsumer<String,String> consumer;
	 private final String topic;
	public ForecastConsumer(String topic) throws IOException
	{	Properties prop = createConsumerConfig();
		this.consumer = new KafkaConsumer<>(prop);
		this.topic = topic;
		this.consumer.subscribe(Arrays.asList(this.topic));
	}
	 private static Properties createConsumerConfig() throws IOException
	 {
	 try (InputStream props = Resources.getResource("consumer.props").openStream()) {
     Properties properties = new Properties();
     properties.load(props);
     return properties;
	 }
	 }
	 @Override
	 public void run() {
	   while (true) {
	     ConsumerRecords<String, String> records = consumer.poll(100);
	     for (final ConsumerRecord<String, String> record : records) {
	    	 //System.out.println(record.key().toString());
	    	 logger.info("Received key is : "+record.key().toString());
	  
	    
			try {
				 ForecastProducer producerThread = new ForecastProducer(DecisionConstants.YES_TOPIC,DecisionConstants.DECISION_YES,record.key().toString(),record.value().toString());
				Thread t1 = new Thread(producerThread);
		    	 t1.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	     consumer.commitSync();
	   }
	 }

	 }


