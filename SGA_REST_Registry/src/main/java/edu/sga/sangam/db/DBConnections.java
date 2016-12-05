package edu.sga.sangam.db;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class DBConnections {
	
	private static DBConnections instance = null;
	private static MongoClient mongo = null;
	
	private DBConnections()
	{
		try{
		    Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
			mongoLogger.setLevel(Level.SEVERE); 
			mongo = new MongoClient(DBConstants.DB_HOST,DBConstants.DB_Port);
                    /*Arrays.asList(new ServerAddress(DBConstants.gateway,DBConstants.DB_Port),
                                  new ServerAddress(DBConstants.worker1,DBConstants.DB_Port),
                                  new ServerAddress(DBConstants.worker2,DBConstants.DB_Port)));*/
			
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
