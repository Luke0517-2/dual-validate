/**
 * 
 */
package cht.bss.morder.dual.validate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jerry
 *
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "dual-validate.baseurl", ignoreInvalidFields = true)
public class DualValidateProperties {
	
	private String iisi;
	
	private String cht;

}
