package edu.sga.sangam.services;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sga.sangam.bean.ResultResponseBean;




public class ResultDeserializer implements Deserializer<ResultResponseBean>  {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResultResponseBean deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		ResultResponseBean  response = null;
		try {
		      response = mapper.readValue(data, ResultResponseBean.class);
		    } catch (Exception e) {
		 
		      e.printStackTrace();
		    }
		    return response;
		  }
	

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	

}
