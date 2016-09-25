package edu.sga.sangam.db;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

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
	public boolean insertFile(File file,String fileName) throws Exception
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			GridFS gridfs = new GridFS(database,"collection_weather");	
			GridFSInputFile gfsFile = gridfs.createFile(file);
			gfsFile.setFilename(fileName);
			gfsFile.save();
			return true;
			
		}
		catch(MongoException me)
		{
			logger.warn(me.getMessage());
			if(me instanceof DuplicateKeyException)
			{
				return false;
			}
			else{
				throw new Exception(me.getMessage());
			}
		}
		finally
		{
			if(mongo!=null)
				mongo.close();
		}
	}
	
	public boolean checkFile(String fileName)
	{
		MongoClient mongo = null;
		try
		{
			mongo = DBConnections.getInstance().getConnection();
			DB database = mongo.getDB("db_SGA");
			DBCollection  collection = database.getCollection("collection_weather.files");
			BasicDBObject query = new BasicDBObject();
			query.put("filename", fileName);
			int count = collection.find(query).count();
			if(count >0)
				return true;
			else
				return false;
		}catch(MongoException me)
		{
			throw me;
		}
		
		finally
		{
			if(mongo!=null)
				mongo.close();
		}
	}
	
}
