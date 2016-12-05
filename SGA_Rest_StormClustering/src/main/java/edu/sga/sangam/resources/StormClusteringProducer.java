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
import org.apache.commons.io.FilenameUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.common.io.Resources;

import edu.sga.sangam.services.StormClusteringService;

public class StormClusteringProducer implements Runnable{
	final static Logger logger = Logger.getLogger(StormClusteringProducer.class);
	private final KafkaProducer<String, String> producer;
	private final String topic;
	private String key;
	private String url;
	
	public StormClusteringProducer(String topic, String key, String url) throws IOException
	{
		Properties prop = createProducerConfig();
		this.producer = new KafkaProducer<String, String>(prop);
		this.key = key;
		this.topic = topic;
		this.url = url;
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
		final String fileName = FilenameUtils.getBaseName(url);
		logger.info("filename is "+fileName);
		StormClusteringService sc = new StormClusteringService();
		File file = sc.generateKmlFile(fileName);
		String filecontents = FileUtils.readFileToString(file,"UTF-8");
		JSONObject stormcluster = new JSONObject();
			try
			{
				producer.send(new ProducerRecord<String, String>(topic, key, fileName), new Callback() {
					public void onCompletion(RecordMetadata metadata, Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						logger.info("Sent:" + fileName);	
					}
				});
				stormcluster.put("key", key);
				stormcluster.put("stormcluster", "Response returned is a file " + fileName);
				registry(stormcluster);
				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			}
			catch(Exception e)
			{
				logger.error(e.getMessage());
				//System.out.println(e.getMessage());
			}
			
			
		// closes producer
		producer.close();

	}

	public void registry(JSONObject requestDataIngestor) throws IOException {
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		requestDataIngestor.put("sctime", df2.format(date));
		HttpClient client = new HttpClient();
		logger.info("calling storm cluster registry");
		ZooKeeperService services =  new ZooKeeperService();
		String registryURL = services.discoverServiceURI("registry");
		PostMethod post = new PostMethod(registryURL+"/stormcluster");
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
				logger.info("Storm Clustering registry status code "+statusCode);
			}
			else
			{
				logger.error("Storm Clustering registry status code "+statusCode);
				throw new IOException(post.getResponseBodyAsString());
				
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new IOException(e.getMessage());
		}
	}

}
