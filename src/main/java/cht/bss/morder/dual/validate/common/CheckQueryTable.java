package cht.bss.morder.dual.validate.common;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dynamic.rule", ignoreInvalidFields = true)
public class CheckQueryTable {

	private List<String> listOfQueryTable;
	
}
