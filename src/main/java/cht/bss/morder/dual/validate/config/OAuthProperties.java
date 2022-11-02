package cht.bss.morder.dual.validate.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *
 */
@Getter
@Setter
public class OAuthProperties {

	/**
	 * token URL
	 */
	private String tokenUrl;

	/**
	 * client ID
	 */
	private String clientId;
	
	/**
	 * client secret
	 */
	private String clientSecret;

	/**
	 * time out second
	 */
	private int timeoutSec;
}
