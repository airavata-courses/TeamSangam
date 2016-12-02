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
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.common.io.Resources;

import edu.sga.sangam.services.RunForecastService;

public class RunForecastProducer implements Runnable {
	final static Logger logger = Logger.getLogger(RunForecastProducer.class);
	private final KafkaProducer<String, String> producer;
	private final String topic;
	private String key;
	private String clusterDetails;
	
	public RunForecastProducer(String topic, String key, String clusterDetails) throws IOException
	{
		Properties prop = createProducerConfig();
		this.producer = new KafkaProducer<String, String>(prop);
		this.key = key;
		this.topic = topic;
		this.clusterDetails = clusterDetails;
	}
	
	private static Properties createProducerConfig() throws IOException {
		 try (InputStream props = Resources.getResource("producer.props").openStream()) {
	            Properties properties = new Properties();
	            properties.load(props);
	           return properties;
	        }
	}	
	@Override
	public void run() {
		try
		{
		byte[] bytes = IOUtils.toByteArray(clusterDetails);
		RunForecastService rfs = new RunForecastService();
		final String jsonString = rfs.getForecastDetails("USA", bytes);
				
		JSONObject stormcluster = new JSONObject();
			try
			{
				producer.send(new ProducerRecord<String, String>(topic, key, jsonString), new Callback() {
					public void onCompletion(RecordMetadata metadata, Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						logger.info("Sent a JsonString to result topic:" + key);	
					}
				});
				stormcluster.put("key", key);
				stormcluster.put("runforecast", "Response returned is a jsonString ");
				registry(stormcluster);
				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			}
			catch(Exception e)
			{
				logger.error(e.getMessage());
				System.out.println(e.getMessage());
			}
		// closes producer
		producer.close();

	}

	public void registry(JSONObject requestDataIngestor) throws IOException {
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		requestDataIngestor.put("rftime", df2.format(date));
		HttpClient client = new HttpClient();
		logger.info("logging run forecast details");
		PostMethod post = new PostMethod("http://localhost:8080/SGA_REST_Registry/sga/registry/runforecast");
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
			logger.info("Run Forecast Resigstry status code  "+statusCode);
				//System.out.println("status code was "+statusCode);
			}
			else
			{
				throw new IOException(post.getResponseBodyAsString());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new IOException(e.getMessage());
		}
	}
}
