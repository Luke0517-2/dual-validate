package cht.bss.morder.dual.validate.config;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Jerry
 *
 */
@Configuration
@EnableCaching
public class CachingConfig {
	
	/**
	 * @return CacheManager
	 */
	@Bean
	public CacheManager getCachingManager() {
//		SimpleCacheManager manager = new SimpleCacheManager();
//		List<CaffeineCache> cachingList = new ArrayList<>();
//		manager.setCaches(null);
		
		ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager();
		manager.setCacheNames(Arrays.asList("token"));
		return manager;
	}

}
