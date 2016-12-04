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

import edu.sga.sangam.services.DataIngestorService;
import edu.sga.sangam.services.DataIngestorServiceKafka;


public class DataIngestorProducer implements Runnable {
	final static Logger logger = Logger.getLogger(DataIngestorProducer.class);
	private final KafkaProducer<String, String> producer;
	private final String topic;
	private String key;
	private String year;
	private String month;
	private String day;
	private String nexrad;
	private String fileName;
	private String userid;
	private String sessionid;
	private String requestid;

	public DataIngestorProducer(String topic, String key, String year, String month, String day,
			String nexrad, String fileName, String userid, String sessionid, String requestid) throws IOException {
		Properties prop = createProducerConfig();
		this.producer = new KafkaProducer<String, String>(prop);
		this.key = key;
		this.topic = topic;
		this.year = year;
		this.month = month;
		this.day = day;
		this.nexrad = nexrad;
		this.fileName = fileName;
		this.userid = userid;
		this.sessionid = sessionid;
		this.requestid = requestid;

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
		DataIngestorServiceKafka dc = new DataIngestorServiceKafka();
		try {
			final String url = dc.generateURL(year, month, day, nexrad, fileName, userid, sessionid, requestid);
			JSONObject requestDataIngestor = new JSONObject();
			requestDataIngestor.put("key", key);
			requestDataIngestor.put("dataingestor", "Response returned is " + url);
			try
			{
				
				producer.send(new ProducerRecord<String, String>(topic, key, url), new Callback() {
					public void onCompletion(RecordMetadata metadata, Exception e) {
						if (e != null) {
							e.printStackTrace();
						}
						logger.info("Sent:" + url);	
					}
				});
				requestDataIngestor.put("key", key);
				requestDataIngestor.put("dataingestor", "Response returned is " + url);
				
				registry(requestDataIngestor);
				
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			
			
		// closes producer
		producer.close();

	}

	public void registry(JSONObject requestDataIngestor) throws IOException {
		logger.info("calling Data Ingestor registry");
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		requestDataIngestor.put("dttime", df2.format(date));
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://ec2-54-193-9-114.us-west-1.compute.amazonaws.com:8080/SGA_REST_Registry/sga/registry/dataingestor");
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
				logger.info("status code of Data Ingestor Registry "+statusCode);
			}
			else
			{
				throw new IOException(post.getResponseBodyAsString());
			}
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}

}
