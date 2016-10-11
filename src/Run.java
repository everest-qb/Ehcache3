import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class Run {

	public static void main(String[] args) {
		CacheManager cacheManager
	    = CacheManagerBuilder.newCacheManagerBuilder() 
	    .withCache("preConfigured",
	        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10))) 
	    .build(); 
	cacheManager.init(); 

	Cache<Long, String> preConfigured =
	    cacheManager.getCache("preConfigured", Long.class, String.class); 

	Cache<Long, String> myCache = cacheManager.createCache("myCache", 
	    CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(10)).build());

	myCache.put(1L, "da one!"); 
	String value = myCache.get(1L); 
	System.out.println(value);
	cacheManager.removeCache("preConfigured"); 

	cacheManager.close();

	}

}
