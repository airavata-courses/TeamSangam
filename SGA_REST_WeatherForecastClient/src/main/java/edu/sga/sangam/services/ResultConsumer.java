package edu.sga.sangam.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.common.io.Resources;

import edu.sga.sangam.client.ZooKeeperClient;


public class ResultConsumer implements Runnable {
	final static Logger logger = Logger.getLogger(ResultConsumer.class);
	 private final KafkaConsumer<String,String> consumer;
	 private final String topic;
	public ResultConsumer(String topic) throws IOException
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
	    	
	    	 logger.info("recieved key from result" +record.key().toString());
	         //System.out.println("Receive: " + record.value().toString());
	    	 
	         JSONObject finaloutput= new JSONObject();
	         finaloutput.put("keyid", record.key().toString());
	         finaloutput.put("value", record.value().toString());
	         try {
				String statuscode = registry(finaloutput);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	     
	     consumer.commitSync();
	   }
	 }catch(WakeupException e)
	{	
		 
	}finally
	{
		consumer.close();
	}
	 }
	 
	 public String registry(JSONObject requestDataIngestor) throws IOException {
			Date date = new Date();
			DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			requestDataIngestor.put("resulttime", df2.format(date));
			HttpClient client = new HttpClient();
			logger.info("calling result registry");
			ZooKeeperClient service = new ZooKeeperClient();
			String registryURL = service.discoverServiceURI("registry");
			PostMethod post = new PostMethod(registryURL+"/result");
			StringRequestEntity entity;
			try {
				entity = new StringRequestEntity(requestDataIngestor.toJSONString(), "application/json", "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				throw new UnsupportedEncodingException("Issue with Encoding");
			}
			post.setRequestEntity(entity);
			post.addRequestHeader("Content-Type", "application/json");
			int statusCode;
			try {
				
				statusCode = client.executeMethod(post);
				if(statusCode == 200)
				{
				// log.info("Data Ingestor Resigstry status code is "+statusCode);
					logger.info("result status code :"+statusCode);
					return "ok";
				}
				else
				{
					logger.error("result status code "+ statusCode);
					throw new IOException(post.getResponseBodyAsString());
				}
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}
		}

}
