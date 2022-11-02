package cht.bss.morder.dual.validate.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
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
		SimpleCacheManager manager = new SimpleCacheManager();

		return manager;
	}

}
