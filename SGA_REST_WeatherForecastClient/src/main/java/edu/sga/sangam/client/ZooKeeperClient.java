package edu.sga.sangam.client;

import java.io.Closeable;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.UriSpec;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import com.google.common.collect.Lists;



public class ZooKeeperClient {
	private final CuratorFramework curatorFramework;
		public ZooKeeperClient()
		{
			 try {
		            //Properties props = new Properties();
		            //props.load(this.getClass().getResourceAsStream("/zookeeper.properties"));
		            curatorFramework = CuratorFrameworkFactory
		                    .newClient("localhost" 
		                            + ":" 
		                            + "2181", new RetryNTimes(5, 1000));
		            curatorFramework.start();
		        } catch (Exception ex) {
		            throw new RuntimeException(ex.getLocalizedMessage());
		        }
		}
		
		 public String discoverServiceURI(String name) {
		        try {
		            String znode = "/services/"+name ;

		            List<String> uris = curatorFramework.getChildren().forPath(znode);
		            
		            for (String uri:uris)
		            {
		            	System.out.println(uri);
		            }
		            Random r = new Random();
		            int n = r.nextInt(uris.size());
		            System.out.println(new String(curatorFramework.getData().forPath(ZKPaths.makePath(znode, uris.get(n)))));
		            return new String(curatorFramework.getData().forPath(ZKPaths.makePath(znode, uris.get(n))));
		        } catch (Exception ex) {
		            throw new RuntimeException("Service \"" + name + "\" not found: " + ex.getLocalizedMessage());
		        }
		    }

			 
	}
	
