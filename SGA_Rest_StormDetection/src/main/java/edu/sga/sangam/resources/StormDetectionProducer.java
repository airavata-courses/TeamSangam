package edu.sga.sangam.resources;

import java.io.File;
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
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.common.io.Resources;

import edu.sga.sangam.services.StormDetectionService;

public class StormDetectionProducer implements Runnable {
	final static Logger logger = Logger.getLogger(StormDetectionProducer.class);
	private final KafkaProducer<String, String> producer;
	private final String topic;
	private String key;
	private String fileName;
	
	public StormDetectionProducer(String topic, String key, String fileName) throws IOException
	{
		Properties prop = createProducerConfig();
		this.producer = new KafkaProducer<String, String>(prop);
		this.key = key;
		this.topic = topic;
		this.fileName = fileName;
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
		StormDetectionService sd = new StormDetectionService();
		
		File file = sd.generateKMLFile(fileName, fileName.substring(0,4));
		String filecontents = FileUtils.readFileToString(file,"UTF-8");
		JSONObject stormdetection = new JSONObject();
			try
			{
				producer.send(new ProducerRecord<String, String>(topic, key, filecontents), new Callback() {
					public void onCompletion(RecordMetadata metadata, Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						logger.info("Sent:" + fileName);	
					}
				});
				stormdetection.put("key", key);
				stormdetection.put("stormdetection", "Response returned is a file with " + fileName.substring(0,4));
				registry(stormdetection);
				
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
		// closes producer
		producer.close();

	}
	public void registry(JSONObject requestDataIngestor) throws IOException {
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		requestDataIngestor.put("sdtime", df2.format(date));
		HttpClient client = new HttpClient();
		logger.info("calling storm detection registry");
		PostMethod post = new PostMethod("http://localhost:8080/SGA_REST_Registry/sga/registry/stormdetection");
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
				logger.info("Storm Detection status code  "+statusCode);
			}
			else
			{
				logger.error("Storm Detection status code  "+statusCode);
				throw new IOException(post.getResponseBodyAsString());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

}
