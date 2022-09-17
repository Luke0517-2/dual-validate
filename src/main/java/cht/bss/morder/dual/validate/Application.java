package cht.bss.morder.dual.validate;


import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
 

/**
 * @author <a href="mailto:jerry.chang@iisigroup.com">Jerry Chang</a>
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 * @author <a href="mailto:hsin.chen@iisigroup.com">Hsin Chen</a>
 */
@SuppressWarnings("PMD")
@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent>{
	/** The log. */
	private static final Logger LOGGER  = LoggerFactory.getLogger(Application.class);
	
	@Value("${app.description: }") 
	private String description;
	
    @Value("${git.commit.message.short}")
    private String commitMessage;

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id}")
    private String commitId;    
    
    @Value("${git.tags}")
    private String tags;
    
    @Value("${git.commit.user.name}")
    private String author;
	
	private final Integer connectionPoolSize;
	
	@Autowired
	public Application(@Value("${pdc-validate.api.maximum-pool-size:4}") Integer connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
		 
	}
    
	/**
	 * @param args 啟動時額外的參數
	 */
	public static void main(final String[] args) {		
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		LOGGER.info("--------------------------------");
		LOGGER.info("Created version: {}",description);
		LOGGER.info("Commit branch: {}",branch);		
		LOGGER.info("Commit message: {}",commitMessage);
		LOGGER.info("Commit id: {}",commitId);		
		LOGGER.info("Author: {}",author);
		LOGGER.info("Tags: {}",tags);
		LOGGER.info("--------------------------------");
	}
	
	/***
	 *  一定只能使用static ，以避免 此spring bean沒有複寫(overwriting)預設spring-bean
	 * **/
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propsConfig 
          = new PropertySourcesPlaceholderConfigurer();
        propsConfig.setLocation(new ClassPathResource("git.properties"));
        
        //設定springExr找不到時，部會爆出錯誤訊息
        propsConfig.setIgnoreResourceNotFound(true);
        propsConfig.setIgnoreUnresolvablePlaceholders(true);
        return propsConfig;
    }
	

	@Bean
	public Scheduler blockingScheduler() {
		LOGGER.info("Creates a blockingScheduler with connectionPoolSize = " + connectionPoolSize);
		return Schedulers.fromExecutor(Executors.newFixedThreadPool(connectionPoolSize));
	}
}
