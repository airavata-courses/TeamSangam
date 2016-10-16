package edu.sga.sangam.db;

import java.io.File;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import edu.sga.sangam.resources.DataIngestRequest;
import edu.sga.sangam.resources.ForecastDecisionBean;
import edu.sga.sangam.resources.RunForecastBean;
import edu.sga.sangam.resources.StormClusterBean;
import edu.sga.sangam.resources.StormDetectionBean;

public class DBOperations {
	private static Logger logger = Logger.getLogger(DBOperations.class);
	private static DBOperations instance = null;
	
	private DBOperations(){
	}
	
	static{
		instance = new DBOperations();
	}

	public static DBOperations getInstance() {
		return instance;
	}
	public void dataIngestRequest(DataIngestRequest input) throws Exception
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			DBCollection collection = database.getCollection("collection_dataingestregistry");
			BasicDBObject document = new BasicDBObject();
			document.put("userid", input.getUserid());
			document.put("sessionid", input.getSessionid());
			document.put("requestid", input.getRequestid());
			document.put("request data", input.getRequestData());
			document.put("response data", input.getResponseData());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			throw new Exception("issue with Data Ingestor request registry");
		}
		finally
		{
			
		}
	}
	public void stormCluster(StormClusterBean input) throws Exception
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			DBCollection collection = database.getCollection("collection_stormclusterregistry");
			BasicDBObject document = new BasicDBObject();
			document.put("userid", input.getUserid());
			document.put("sessionid", input.getSessionid());
			document.put("requestid", input.getRequestid());
			document.put("request data", input.getRequestData());
			document.put("response data", input.getResponseData());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			throw new Exception("issue with Storm Cluster request registry");
		}
		finally
		{
			
		}
	}
	
	
	public void stormDetection(StormDetectionBean input) throws Exception
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			DBCollection collection = database.getCollection("collection_stormdetectionregistry");
			BasicDBObject document = new BasicDBObject();
			document.put("userid", input.getUserid());
			document.put("sessionid", input.getSessionid());
			document.put("requestid", input.getRequestid());
			document.put("request data", input.getRequestData());
			document.put("response data", input.getResponseData());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			throw new Exception("issue with Storm Detection registry");
		}
		finally
		{
			
		}
	}
	
	public void forecastDecision(ForecastDecisionBean input) throws Exception
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			DBCollection collection = database.getCollection("collection_decisionregistry");
			BasicDBObject document = new BasicDBObject();
			document.put("userid", input.getUserid());
			document.put("sessionid", input.getSessionid());
			document.put("requestid", input.getRequestid());
			document.put("request data", input.getRequestData());
			document.put("response data", input.getResponseData());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			throw new Exception("issue with Forecast Decision registry");
		}
		finally
		{
			
		}
	}
	
	public void runForecast(RunForecastBean input) throws Exception
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			DBCollection collection = database.getCollection("collection_runforecastregistry");
			BasicDBObject document = new BasicDBObject();
			document.put("userid", input.getUserid());
			document.put("sessionid", input.getSessionid());
			document.put("requestid", input.getRequestid());
			document.put("request data", input.getRequestData());
			document.put("response data", input.getResponseData());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			throw new Exception("issue with Run Forecastregistry");
		}
		finally
		{
			
		}
	}
	
}
