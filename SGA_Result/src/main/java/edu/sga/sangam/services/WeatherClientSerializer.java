package edu.sga.sangam.services;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sga.sangam.bean.DataIngestorRequest;

public class WeatherClientSerializer implements Serializer<DataIngestorRequest>{

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub	
	}

	@Override
	public byte[] serialize(String topic, DataIngestorRequest data) {
		byte[] dataIngestorRequest = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        dataIngestorRequest = objectMapper.writeValueAsString(data).getBytes();
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	      return dataIngestorRequest;    
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	
}