package cht.bss.morder.dual.validate.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dynamic.rule", ignoreInvalidFields = true)
public class CheckQueryTableProperties {

	private List<String> listOfQueryTable;
	
}
