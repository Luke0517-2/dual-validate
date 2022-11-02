package cht.bss.morder.dual.validate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "transfer.property", ignoreInvalidFields = true)
public class TransferProperties   {
	
	/**
	 * mOrder
	 */
	@NestedConfigurationProperty
	private TransferUnitProperties mOrder;
	
	private String charset;
}
