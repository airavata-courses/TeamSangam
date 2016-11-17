package edu.sga.sangam.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.AggregateIterable;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

import edu.sga.sangam.resources.DataIngestRequest;
import edu.sga.sangam.resources.ForecastDecisionBean;
import edu.sga.sangam.resources.GetStatsBean;
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
			document.put("request time",input.getRequestTime());
			document.put("response data", input.getResponseData());
			document.put("response time", input.getResponseTime());
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
			document.put("request time",input.getRequestTime());
			document.put("response data", input.getResponseData());
			document.put("response time", input.getResponseTime());
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
			document.put("request time",input.getRequestTime());
			document.put("response data", input.getResponseData());
			document.put("response time", input.getResponseTime());
			
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
			document.put("request time",input.getRequestTime());
			document.put("response data", input.getResponseData());
			document.put("response time", input.getResponseTime());
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
			document.put("request time",input.getRequestTime());
			document.put("response data", input.getResponseData());
			document.put("response time", input.getResponseTime());
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
	
	public String getStats() throws Exception
    {
	    
	    MongoClient mongoClient = new MongoClient();
	    MongoDatabase db = mongoClient.getDatabase("db_SGA");
	    System.out.println("Connected to database successfully");
	    
	    //FindIterable<Document> iterable = db.getCollection("collection_dataingestregistry").find(new Document("userid", input.getUserid()));
	    
	    StringBuffer queryResult = new StringBuffer();
	    List<String> result = new ArrayList<>();
	    
	    AggregateIterable<Document> iterable1 = db.getCollection("collection_dataingestregistry").aggregate(asList(
	            new Document("$group", new Document("_id", "$userid").append("count", new Document("$sum", 1)))));
	    
	    System.out.println("User statistics from dataingestregistry:");
	    for(Document row: iterable1){
	        queryResult.append(row.toJson());
	    }
	    
	    AggregateIterable<Document> iterable2 = db.getCollection("collection_stormclusterregistry").aggregate(asList(
                new Document("$group", new Document("_id", "$userid").append("count", new Document("$sum", 1)))));
	    System.out.println("User statistics from stormclusterregistry:");
	    for(Document row: iterable2){
            queryResult.append(row.toJson());
        }
	    
	    AggregateIterable<Document> iterable3 = db.getCollection("collection_decisionregistry").aggregate(asList(
                new Document("$group", new Document("_id", "$userid").append("count", new Document("$sum", 1)))));
	    System.out.println("User statistics from decisionregistry:");
	    for(Document row: iterable3){
            queryResult.append(row.toJson());
        }
        
        AggregateIterable<Document> iterable4 = db.getCollection("collection_runforecastregistry").aggregate(asList(
                new Document("$group", new Document("_id", "$userid").append("count", new Document("$sum", 1)))));
        System.out.println("User statistics from runforecastregistry:");
        for(Document row: iterable4){
            queryResult.append(row.toJson());
        }
        
        return queryResult.toString();
    }
	
}
