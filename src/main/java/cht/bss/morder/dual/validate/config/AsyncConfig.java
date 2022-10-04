/**
 * 
 */
package cht.bss.morder.dual.validate.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:jerry.chang@iisigroup.com">Jerry Chang</a>
 *
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
	
	/**
	 * The search url.
	 */
	@Value("${async.executor.corepool}")
	private int corepool;

	@Value("${async.executor.maxpool}")
	private int maxpool;
	
	@Value("${async.executor.queueCapacity}")
	private int queueCapacity;
	
	@Value("${async.executor.keepAliveSeconds}")
	private int keepAliveSeconds;
	
	@Value("${async.executor.allowCoreThreadTimeOut}")
	private String allowCoreThreadTimeOut;
	
	@Override
    public Executor getAsyncExecutor() {
		log.info("core:{},max:{},queue{}",corepool, maxpool, queueCapacity);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corepool);
        executor.setMaxPoolSize(maxpool);
        executor.setQueueCapacity(queueCapacity);
        executor.setAllowCoreThreadTimeOut(Boolean.valueOf(allowCoreThreadTimeOut));
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("MOrderExecutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    	return (ex, method, params) -> {
            log.error("Exception with message :" + ex.getMessage());
            log.trace("Method :" + method.toString());
            log.trace("Number of parameters :" + params.length);
        };
    }
}
