package cht.bss.morder.dual.validate.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@ConfigurationProperties(prefix = "dynamic.rule", ignoreInvalidFields = true)
public class DynamicRuleSampleProperties   {
//	@Value("#{${dynamic.rule.rules}}")
	private List<String> rules;


}
