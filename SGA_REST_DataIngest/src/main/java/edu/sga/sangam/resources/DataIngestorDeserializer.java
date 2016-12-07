package edu.sga.sangam.resources;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DataIngestorDeserializer implements Deserializer<DataIngestResponse> {
	 
	  @Override
	  public void close() {
	 
	  }
	 
	  @Override
	  public void configure(Map<String, ?> arg0, boolean arg1) {
	 
	  }
	 
	  @Override
	  public DataIngestResponse deserialize(String arg0, byte[] arg1) {
	    ObjectMapper mapper = new ObjectMapper();
	    DataIngestResponse response =null;
	    try {
	      response = mapper.readValue(arg1, DataIngestResponse.class);
	    } catch (Exception e) {
	 
	      e.printStackTrace();
	    }
	    return response;
	  }
	 
	}