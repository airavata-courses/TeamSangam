package edu.sga.sangam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.log4j.Logger;

import com.google.common.io.Resources;

public class StormDetectionConsumer implements Runnable {
	final static Logger logger = Logger.getLogger(StormDetectionConsumer.class);
	 private final KafkaConsumer<String,String> consumer;
	 private final String topic;
	public StormDetectionConsumer(String topic) throws IOException
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
			try{
			   while (true) {
			     ConsumerRecords<String, String> records = consumer.poll(100);
			     for (final ConsumerRecord<String, String> record : records) {
			    	logger.info("recievied key from SC "+record.key().toString() +"value is :"+record.value().toString());
			         StormDetectionProducer producerThread= new StormDetectionProducer("forecast",record.key().toString(),record.value().toString());
			    	 Thread t1 = new Thread(producerThread);
			    	 t1.start();
			    	 }
			     
			     consumer.commitSync();
			   }
			 }catch(IOException e)
			{	
				 
			}finally
			{
				consumer.close();
			}
			 }
}


