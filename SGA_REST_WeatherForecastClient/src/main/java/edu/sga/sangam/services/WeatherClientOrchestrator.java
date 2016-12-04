package edu.sga.sangam.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.io.Resources;

import edu.sga.sangam.bean.ClientOrchestratorBean;
import edu.sga.sangam.bean.DataIngestorBean;
import edu.sga.sangam.bean.DataIngestorRequest;
import edu.sga.sangam.bean.StormClusterBean;
import edu.sga.sangam.bean.StormDetectionBean;
import edu.sga.sangam.client.ZooKeeperClient;

public class WeatherClientOrchestrator {
	ZooKeeperClient zc = new ZooKeeperClient();	
	public String clientOrchestrator(String year,String mm,String day,String nexrad,String fileName,String userid,String sessionid,
			String requestid) throws Exception
	{
		try
		{
			String key = callDataIngestor(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
			return key;	
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}
	}

	public String callDataIngestor(String year,String mm,String day,String nexrad,String fileName,String userid,String sessionid,String requestid ) throws Exception
	{
		
		DataIngestorRequest db = new DataIngestorRequest(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
		KafkaProducer<String, DataIngestorRequest> producer;
		String topic = "dataingest";
		/*Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
	    props.put("acks", "all");
	    props.put("retries", 0);
	    props.put("batch.size", 16384);
	    props.put("linger.ms", 1);
	    props.put("buffer.memory", 33554432);
	    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	    props.put("value.serializer", "edu.sga.sangam.services.WeatherClientSerializer"); */
	    try (InputStream props = Resources.getResource("producer.props").openStream()) {
            Properties properties = new Properties();
            properties.load(props);
            producer = new KafkaProducer<>(properties);
        }
	    UUID text = UUID.randomUUID();
	    final String key = userid+"_"+text;
	    final JSONObject request = new JSONObject();
	    request.put("key", key);
	    request.put("userid", db.getUserid());
	    request.put("sessionid", db.getSessionid());
	    request.put("requestid", db.getRequestid());
	    request.put("requestData", "requested data for year"+db.getYear()+ "month" + db.getMonth() +"day" +db.getDay()+ "nexrad" +db.getNexrad());
	    request.put("orchestrator", "request success");
	    try
	    {
	    	registry(request);
	    }catch(Exception e)
	    {
	    	throw new Exception(e.getMessage());
	    }
	    System.out.println(key);
	    producer.send(new ProducerRecord<String, DataIngestorRequest>(topic, key, db) ,new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception e)  {
                if (e != null) {
                  e.printStackTrace();
                }
				
                System.out.println("Sent:" + key);  
              }
            });
	    producer.close();
	    return key;
	}

	public void registry(JSONObject request) throws IOException {
		Date date = new Date();
		DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		request.put("time", df2.format(date));
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://localhost:8085/SGA_REST_Registry/sga/registry/orchestrator");
		StringRequestEntity entity;
		try {
			entity = new StringRequestEntity(request.toJSONString(), "application/json", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new UnsupportedEncodingException("Issue with Encoding");
		}
		post.setRequestEntity(entity);
		post.addRequestHeader("Content-Type", "application/json");
		int statusCode;
		try {
			//System.out.println("calling registry");
			statusCode = client.executeMethod(post);
			if(statusCode == 200)
			{		
				//System.out.println("success");
			}
			else
			{	
				throw new IOException(post.getResponseBodyAsString());
			}
			// log.info("Data Ingestor Resigstry status code is "+statusCode);
		} catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}
}
