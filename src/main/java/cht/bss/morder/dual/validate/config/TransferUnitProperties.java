package cht.bss.morder.dual.validate.config;
 
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
public class TransferUnitProperties   {
	
	/**
	 * API getway properties
	 */
	private ApiGateWayProperties apigwProperties;
	
	/**
	 * oauth properties
	 */
	private OAuthProperties oauthProperties;
}
