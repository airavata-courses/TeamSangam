package edu.sga.sangam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import com.google.common.io.Resources;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;

public class DataIngestorConsumer implements Runnable {
	final static Logger logger = Logger.getLogger(DataIngestorConsumer.class);
	 private final KafkaConsumer<String, DataIngestResponse> consumer;
	 private final String topic;
	public DataIngestorConsumer(String topic) throws IOException
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
	     ConsumerRecords<String, DataIngestResponse> records = consumer.poll(100);
	     for (final ConsumerRecord<String, DataIngestResponse> record : records) {
	    	 logger.info("received key "+record.key().toString() + "value "+ record.value().toString() + "and offset "+record.offset());
	         String [] params = record.value().toString().split(",");
	    	 DataIngestorProducer producerThread;
			try {
				producerThread = new DataIngestorProducer("stormclustering",record.key().toString(),params[0],params[1],params[2],params[3],
						 params[4],params[5],params[6],params[7]);
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
