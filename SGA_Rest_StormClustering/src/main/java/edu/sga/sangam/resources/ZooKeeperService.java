package edu.sga.sangam.resources;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

public class ZooKeeperService  {
	private final CuratorFramework curatorFramework;
    private final ConcurrentHashMap<String, String> uriToZnodePath;
	
	@Inject
    public ZooKeeperService() {
        try {
            //Properties props = new Properties();
            //props.load(this.getClass().getResourceAsStream("/zookeeper.properties"));
            curatorFramework = CuratorFrameworkFactory
                    .newClient("localhost" 
                            + ":" 
                            + "2181", new RetryNTimes(5, 1000));
            curatorFramework.start();
            uriToZnodePath = new ConcurrentHashMap<>();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getLocalizedMessage());
        }
    }
		
    public void registerService(String name, String uri) {
        try {
            String znode = "/services/stormclustering/" + name;

            if (curatorFramework.checkExists().forPath(znode) == null) {
                curatorFramework.create().creatingParentsIfNeeded().forPath(znode);
            }
            System.out.println(uri);
            String znodePath = curatorFramework
                    .create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(znode);
            System.out.println(znodePath);
            curatorFramework.setData().forPath(znode,uri.getBytes());
            //uriToZnodePath.put("Hello", znode);
        } catch (Exception ex) {
            throw new RuntimeException("Could not register service \"" 
                    + name 
                    + "\", with URI \"" + uri + "\": " + ex.getMessage());
        }
    }
    
    public void unregisterService(String name, String uri) {
        try {
            if (uriToZnodePath.contains(uri)) {
            	System.out.println("deleted");
                curatorFramework.delete().forPath(uriToZnodePath.get(uri));
    
            }
            else
            {
            	System.out.println(uri);
            	System.out.println("uri doenst exists");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not unregister service \"" 
                    + name 
                    + "\", with URI \"" + uri + "\": " + ex.getLocalizedMessage());
        }
    }


}