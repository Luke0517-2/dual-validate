package cht.bss.morder.dual.validate.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *
 */
@Getter
@Setter
public class ApiGateWayProperties {
	/**
	 * base URL
	 */
	private String baseUrl;
	
	/**
	 * API getway URI
	 */
	private String apigwUri;
	
	/**
	 * x api key
	 */
	private String xapikey;
	
	/**
	 * from site
	 */
	private String fromSite;

	/**
	 * from site
	 */
	private String empNo;

	/**
	 * from site
	 */
	private String clientIp;
	
	/**
	 *	 time out second
	 */
	private int timeoutSec;
}
