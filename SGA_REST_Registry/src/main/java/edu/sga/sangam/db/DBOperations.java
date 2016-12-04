package edu.sga.sangam.db;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;

import org.bson.Document;
import com.mongodb.client.AggregateIterable;

import static java.util.Arrays.asList;

import edu.sga.sangam.resources.DataIngestRequest;
import edu.sga.sangam.resources.ForecastDecisionBean;
import edu.sga.sangam.resources.GetStatsBean;
import edu.sga.sangam.resources.OrchestratorBean;
import edu.sga.sangam.resources.ResultBean;
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
			DB database = mongo.getDB(DBConstants.DB_Name);
			DBCollection collection = database.getCollection(DBConstants.DB_Collection_Log);
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append("dataingestor",input.getDataingestor())
					.append("dttime", input.getDttime()));
			collection.update(new BasicDBObject().append("keyid", input.getKey()), document);
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
			DB database = mongo.getDB(DBConstants.DB_Name);
			DBCollection collection = database.getCollection(DBConstants.DB_Collection_Log);
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append("stormcluster",input.getStormcluster())
					.append("sctime", input.getSctime()));
			collection.update(new BasicDBObject().append("keyid", input.getKey()), document);
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
				DB database = mongo.getDB(DBConstants.DB_Name);
				DBCollection collection = database.getCollection(DBConstants.DB_Collection_Log);
				BasicDBObject document = new BasicDBObject();
				document.append("$set", new BasicDBObject().append("stormdetection",input.getStormdetection())
						.append("sdtime", input.getSdtime()));
				collection.update(new BasicDBObject().append("keyid", input.getKey()), document);
			
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
			DB database = mongo.getDB(DBConstants.DB_Name);
			DBCollection collection = database.getCollection(DBConstants.DB_Collection_Log);
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append("forecast",input.getForecast())
					.append("fctime", input.getFctime()));
			collection.update(new BasicDBObject().append("keyid", input.getKey()), document);
		
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
			DB database = mongo.getDB(DBConstants.DB_Name);
			DBCollection collection = database.getCollection(DBConstants.DB_Collection_Log);
			BasicDBObject document = new BasicDBObject();
			document.append("$set", new BasicDBObject().append("runforecast",input.getRunforecast())
					.append("rftime", input.getRftime()));
			collection.update(new BasicDBObject().append("keyid", input.getKey()), document);
		
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			throw new Exception("issue with Run Forecast registry");
		}
		finally
		{
			
		}
	}
	
	public void orchestratorcollection(OrchestratorBean ob) throws Exception{
		MongoClient mongo = null;
		try
		{
			//System.out.println("mongo connection");
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB(DBConstants.DB_Name);
			//System.out.println("mongo connection failed");
			DBCollection collection = database.getCollection(DBConstants.DB_Collection_Log);
			BasicDBObject document = new BasicDBObject();
			document.put("keyid", ob.getKey());
			document.put("userid", ob.getUserid());
			document.put("sessionid", ob.getSessionid());
			document.put("requestid", ob.getRequestid());
			document.put("request data", ob.getRequestData());
			document.put("time",ob.getTime());
			document.put("orchestrator", ob.getOrchestrator());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			me.printStackTrace();
			throw new Exception("issue with orchestrator collection");
		}
		finally
		{
			
		}	
	}
	
	
	public void resultcollection(ResultBean ob) throws Exception{
		MongoClient mongo = null;
		try
		{
			System.out.println("mongo connection");
			mongo = DBConnections.getInstance().getConnection();
			
			DB database = mongo.getDB(DBConstants.DB_Name);
			//System.out.println("mongo connection failed");
			DBCollection collection = database.getCollection(DBConstants.DB_Collection_Result);
			BasicDBObject document = new BasicDBObject();
			document.put("keyid", ob.getKeyid());
			document.put("result", ob.getValue());
			document.put("time", ob.getResultime());
			collection.insert(document);
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			me.printStackTrace();
			throw new Exception("issue with Result Collection");
		}
		finally
		{
			
		}
		
	}
	
	public int getCount(String key) throws Exception
	{
		int count =0;
		MongoClient mongo = null;
		try
			{
				System.out.println("mongo connection");
				mongo = DBConnections.getInstance().getConnection();				
				DB database = mongo.getDB(DBConstants.DB_Name);
				//System.out.println("mongo connection failed");
				DBCollection collection = database.getCollection(DBConstants.DB_Collection_Result);
				BasicDBObject query = new BasicDBObject();
				query.put("keyid", key);
				count = collection.find(query).count();
				return count;
			}
		catch(Exception me)
		{
			logger.warn(me.getMessage());
			me.printStackTrace();
			throw new Exception("issue with Result Collection");
		}
				
			
	}
	public String getResult(String key) throws Exception
	{
		MongoClient mongo = null;
		String result=null;
		try
			{
				System.out.println("mongo connection");
				mongo = DBConnections.getInstance().getConnection();				
				DB database = mongo.getDB(DBConstants.DB_Name);
				//System.out.println("mongo connection failed");
				DBCollection collection = database.getCollection(DBConstants.DB_Collection_Result);
				BasicDBObject query = new BasicDBObject();
				BasicDBObject field = new BasicDBObject();
				query.put("keyid", key);
				field.put("result",1);
				field.put("_id",-1 );
				
				DBCursor cursor = collection.find(query,field);
				while(cursor.hasNext())
				{	
					DBObject db = cursor.next();
					result =(String) db.get("result");
					System.out.println(result);
					break;
				}
				return result;
			}catch(Exception me)
		{
				logger.warn(me.getMessage());
				me.printStackTrace();
				throw new Exception("issue with Result Collection");
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
	
	
	public class ResultThread implements Runnable
	{
		private String result;
		private String key;
		public ResultThread(String key)
		{
			this.key =key;
		}
		@Override
		public void run()
		{
			MongoClient mongo = null;
			try
			{
				System.out.println("mongo connection");
				mongo = DBConnections.getInstance().getConnection();
				
				DB database = mongo.getDB(DBConstants.DB_Name);
				//System.out.println("mongo connection failed");
				DBCollection collection = database.getCollection(DBConstants.DB_Collection_Result);
				BasicDBObject query = new BasicDBObject();
				BasicDBObject field = new BasicDBObject();
				
				query.put("keyid", key);
				field.put("result",1);
				field.put("_id",-1 );
				
				DBCursor cursor = collection.find(query);
				while(cursor.hasNext())
				{	
					DBObject db = cursor.next();
					result =(String) db.get("result");
					System.out.println(result);
					//result =(String) cursor.next().get("result");
					
				}
				cursor.close();
				Thread.sleep(3000);
			}catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		
		public String getValue()
		{
			System.out.println("result is "+result);
			return result;
		}
	}
}
