package edu.sga.sangam.resources;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RunForecastSerializer implements Serializer<RunForecastBean>{ {

}

@Override
public void configure(Map<String, ?> configs, boolean isKey) {
	// TODO Auto-generated method stub
	
}

@Override
public byte[] serialize(String topic, RunForecastBean data) {
	byte[] runForecast = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        runForecast = objectMapper.writeValueAsString(data).getBytes();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return runForecast;  
}

@Override
public void close() {
	// TODO Auto-generated method stub
	
}

}