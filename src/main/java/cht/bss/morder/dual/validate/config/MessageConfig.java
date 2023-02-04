package cht.bss.morder.dual.validate.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *
 */
@Configuration
public class MessageConfig {

	/**
	 * get message Source
	 * @return MessageSource
	 */
	@Bean
	public MessageSource messageSource() {
	    final ReloadableResourceBundleMessageSource messageSource
	      = new ReloadableResourceBundleMessageSource();
	    
	    messageSource.setBasename("errorMessages");
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	}
}
