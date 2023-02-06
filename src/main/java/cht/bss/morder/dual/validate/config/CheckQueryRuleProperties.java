package cht.bss.morder.dual.validate.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "dynamic.rule", ignoreInvalidFields = true)
public class CheckQueryRuleProperties {

	private List<String> queryRuleList;
	
}
