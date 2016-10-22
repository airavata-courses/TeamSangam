package edu.sga.sangam.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;

public class DBConnections {
	
	private static DBConnections instance = null;
	private static MongoClient mongo = null;
	
	private DBConnections()
	{
		try{
		    Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
			mongoLogger.setLevel(Level.SEVERE); 
			mongo = new MongoClient("mongo",27017);
			
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	static{
		instance = new DBConnections();
	}
	
	public static DBConnections getInstance()
	{
		return instance;
	}
	public MongoClient getConnection(){
		return mongo;
	}
}
