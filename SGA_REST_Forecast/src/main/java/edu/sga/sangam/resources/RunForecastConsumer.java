package edu.sga.sangam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

import com.google.common.io.Resources;

public class RunForecastConsumer implements Runnable {
	final static Logger logger = Logger.getLogger(RunForecastConsumer.class);
	 private final KafkaConsumer<String,String> consumer;
	 private final String topic;
	public RunForecastConsumer(String topic) throws IOException
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
	         logger.info("run forecast key :"+record.key().toString() +" value is :"+record.value().toString());
	    	 RunForecastProducer producerThread;
			try {
				producerThread = new RunForecastProducer("result",record.key().toString(),record.value().toString());
				 Thread t1 = new Thread(producerThread);
		    	 t1.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				e.printStackTrace();
			}
	    	

	     }
	     consumer.commitSync();
	   }
	 }

}
