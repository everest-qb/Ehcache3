import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.Status;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

import object.DeviceObj;

public class RunXml {

	final URL myUrl = getClass().getResource("cache.xml");
	private static CacheManager cachManager;
	public RunXml() {
		super();
		XmlConfiguration xmlConfig = new XmlConfiguration(myUrl);
		RunXml.cachManager = CacheManagerBuilder.newCacheManager(xmlConfig);
	}

	public static void main(String[] args) {
		RunXml r=new RunXml();
		r.status();		
		
		Cache<String,String> sysCach=r.findSysCache();
		
		Cache<String,DeviceObj> dataCach=r.findDataCache();
		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		Random random=new Random();
		executor.submit(()->{
			int id=0;						
			while(true){
				DeviceObj obj=new DeviceObj();
				obj.setId("Dev"+id);
				obj.setNumber(id);
				obj.setDetical(random.nextDouble());
				dataCach.putIfAbsent(obj.getId(), obj);
				id++;
				TimeUnit.SECONDS.sleep(1);
			}
			
		});
		
		executor.submit(()->{
			while(true){
				System.out.println("----------");
				dataCach.forEach((Cache.Entry<String,DeviceObj> entry)->{					
					System.out.println(entry.getValue());
				});
				
				TimeUnit.MILLISECONDS.sleep(500);
			}			
		});
		
		
		executor.submit(()->{
			while(true){	
				String id="Dev"+random.nextInt(100);
				if(dataCach.containsKey(id)){
					DeviceObj obj=dataCach.get(id);
					obj.setDetical(random.nextDouble());
					dataCach.replace(id, obj);
					System.out.println(id+" Changed!");
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}			
		});
		
		
		executor.submit(()->{
			while(true){	
				String id="Dev"+random.nextInt(100);
				if(dataCach.containsKey(id)){
					
					dataCach.remove(id);
					System.out.println(id+" Removed!");
				}
				TimeUnit.MILLISECONDS.sleep(200);
			}			
		});
		
		r.status();		
		try {
		   
		    executor.shutdown();
		    executor.awaitTermination(100, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
		    
		}
		finally {
		    if (!executor.isTerminated()) {
		        
		    }
		    executor.shutdownNow();		    
		}
		
		RunXml.close();		
	}

	public void status(){
		Status status=cachManager.getStatus();
		System.out.println(status);	
		
	}
	
	public Cache<String,String> findSysCache(){		
		if(Status.UNINITIALIZED.name().equals(cachManager.getStatus().name())){
			cachManager.init();
		}
		return cachManager.getCache("system_vars", String.class, String.class);
	}
	
	public Cache<String,DeviceObj> findDataCache(){		
		if(Status.UNINITIALIZED.name().equals(cachManager.getStatus().name())){
			cachManager.init();
		}
		return cachManager.getCache("device_data", String.class, DeviceObj.class);
	}
	
	public static void close(){
		if(RunXml.cachManager!=null)
			RunXml.cachManager.close();
	}
}
