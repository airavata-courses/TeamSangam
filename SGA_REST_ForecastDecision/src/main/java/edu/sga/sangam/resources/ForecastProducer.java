package edu.sga.sangam.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.common.io.Resources;

public class ForecastProducer implements Runnable {
	final static Logger logger = Logger.getLogger(ForecastProducer.class);
	private final KafkaProducer<String, String> producer;
	private final String topic;
	private String decision;
	private String key;
	private String fileDetails;
	
	public ForecastProducer(String topic,String decision, String key, String fileDetails) throws IOException
	{
		Properties prop = createProducerConfig();
		this.producer = new KafkaProducer<String, String>(prop);
		this.key = key;
		this.topic = topic;
		this.decision = decision;
		this.fileDetails = fileDetails;
	}
	
	private static Properties createProducerConfig() throws IOException {
		 try (InputStream props = Resources.getResource("producer.props").openStream()) {
	            Properties properties = new Properties();
	            properties.load(props);
	           return properties;
	        }
	}
	
public void run() {
	
	try{
			JSONObject forecast = new JSONObject();
			producer.send(new ProducerRecord<String, String>(topic, key, fileDetails), new Callback() {
				public void onCompletion(RecordMetadata metadata, Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						logger.info("Sent:" + key +"decision is :"+decision);
					}
				});
				//closes producer
				producer.close();
			try
			{
				forecast.put("key", key);
				forecast.put("forecast", "Response returned is " + decision);
				registry(forecast);
				
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new Exception(e.getMessage());
			}
			}
			catch(Exception e)
			{
				logger.error(e.getMessage());
				System.out.println(e.getMessage());
			}
			
		
	}

	public void registry(JSONObject requestDataIngestor) throws IOException {
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		requestDataIngestor.put("fctime", df2.format(date));
		HttpClient client = new HttpClient();
		logger.info("calling forecast registry");
		ZooKeeperService services =  new ZooKeeperService();
		String registryURL = services.discoverServiceURI("registry");
		PostMethod post = new PostMethod(registryURL+"/forecast");
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
				logger.info("status code of forecast registry is "+statusCode);
				//System.out.println("status code was "+statusCode);
			}
			else
			{
				logger.error("status code of forecast registry is "+statusCode);
				throw new IOException(post.getResponseBodyAsString());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new IOException(e.getMessage());
		}
	}
}
	
