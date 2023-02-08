package cht.bss.morder.dual.validate.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "dual-validate", ignoreInvalidFields = true)
public class CheckQueryRuleProperties {

	public List<String> queryRuleList;

}
