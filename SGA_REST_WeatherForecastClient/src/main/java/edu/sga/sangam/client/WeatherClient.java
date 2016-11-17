package edu.sga.sangam.client;

import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.google.common.io.Resources;

import edu.sga.sangam.services.WeatherClientOrchestrator;


@Path("weatherclient")
public class WeatherClient {
	
	@GET
	public Response getURL(@QueryParam("year") String year,@QueryParam("month") String mm,
			@QueryParam("day") String day,@QueryParam("nexrad") String nexrad,@QueryParam("filename") String fileName,@QueryParam("userid") String userid,
			@QueryParam("sessionid") String sessionid,@QueryParam("requestid") String requestid)
	{
		try
		{
			KafkaProducer<String, String> producer;
			System.out.println("reading properties");
			try (InputStream props = Resources.getResource("producer.props").openStream()) {
				 Properties properties = new Properties();
		            properties.load(props);
		            producer = new KafkaProducer<>(properties);
			}
			 try {
		            for (int i = 0; i < 1000; i++) {
		                // send lots of messages
		                producer.send(new ProducerRecord<String, String>(
		                        "fast-messages",
		                        String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));

		                // every so often send to a different topic
		                if (i % 100 == 0) {
		                    producer.send(new ProducerRecord<String, String>(
		                            "fast-messages",
		                            String.format("{\"type\":\"marker\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
		                    producer.send(new ProducerRecord<String, String>(
		                            "summary-markers",
		                            String.format("{\"type\":\"other\", \"t\":%.3f, \"k\":%d}", System.nanoTime() * 1e-9, i)));
		                    producer.flush();
		                    System.out.println("Sent msg number " + i);
		                }
		            }
		        } catch (Throwable throwable) {
		            System.out.printf("%s", throwable.getStackTrace());
		        } finally {
		            producer.close();
		        }

			//WeatherClientOrchestrator wcs = new WeatherClientOrchestrator();
			//String output =wcs.clientOrchestrator(year,mm,day,nexrad,fileName,userid,sessionid,requestid);
			//return Response.status(200).entity(output).build();
			 return Response.status(200).build();
		}catch(Exception e)
		{
			return Response.status(400).entity(e.getMessage()).build();
		}
	}

}
