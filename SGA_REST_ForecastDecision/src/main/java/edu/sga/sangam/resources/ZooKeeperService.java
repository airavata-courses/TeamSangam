package edu.sga.sangam.resources;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
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
            String znode = "/services/forecastdecision/" + name;

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
    public String discoverServiceURI(String name) {
        try {
            String znode = "/services/" + name ;

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
