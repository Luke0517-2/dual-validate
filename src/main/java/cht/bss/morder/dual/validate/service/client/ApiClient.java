/**
 * 
 */
package cht.bss.morder.dual.validate.service.client;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.JsonPath;

import cht.bss.morder.dual.validate.common.exceptions.UnauthorizedException;
import cht.bss.morder.dual.validate.config.ApiGateWayProperties;
import cht.bss.morder.dual.validate.config.OAuthProperties;
import cht.bss.morder.dual.validate.config.TransferProperties;
import cht.bss.morder.dual.validate.config.TransferUnitProperties;
import cht.bss.morder.dual.validate.vo.QueryInput;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author Jerry
 *
 */
@Slf4j
@Service
public class ApiClient {

	String CACHE_NAME_TOKEN = "token";

	@Autowired
	private TransferProperties properties;

	@Autowired
	private OauthClient oauthClient;

	@Autowired
	private ApiGatewayClient apiGatewayClient;

	@Autowired
	private CacheManager ccm;

	public String queryCustInfo(QueryInput input) {
		final TransferUnitProperties transferUnitProperties = properties.getMOrder();
		final OAuthProperties oauthProperties = transferUnitProperties.getOauthProperties();
		final ApiGateWayProperties apigwProperties = transferUnitProperties.getApigwProperties();

		final String xApiKey = apigwProperties.getXapikey();

		String content = null;

		try {
			String token = getToken(oauthProperties);

			Mono<String> mono = apiGatewayClient.getApiDataByJson(getHttpHeaders(xApiKey, token), apigwProperties,
					input);
			content = mono.block();
		} catch (UnauthorizedException e) {
			log.error(e.getMessage(), e);
			clean();
			String token = getToken(oauthProperties);

			Mono<String> mono = apiGatewayClient.getApiDataByJson(getHttpHeaders(xApiKey, token), apigwProperties,
					input);
			content = mono.block();
		}
		return content;
	}

	protected String getToken(final OAuthProperties oAuthProperties) {
		return alternative(CACHE_NAME_TOKEN, oAuthProperties, () -> retrieveTokenV2(oAuthProperties));
	}

	/**
	 * <p>
	 * 當執行spring-native時，會使@Cacheable不能正常運作
	 * <p>
	 * 改使用spring-boot-cache programing 代替
	 **/
	protected String alternative(final String cacheName, final OAuthProperties key,
			final Callable<String> valueLoader) {
		return ccm.getCache(cacheName).get(key, valueLoader);
	}

	protected void clean() {
		ccm.getCache(CACHE_NAME_TOKEN).clear();
		;
	}

	private String retrieveTokenV2(final OAuthProperties oAuthProperties) {
		String data = oauthClient.getToken(oAuthProperties).block();
		log.info("the content of response: {}", data);
		String token = JsonPath.parse(data).read("$.access_token");
		log.info("token:{}", token);

		return token;
	}

	protected Consumer<HttpHeaders> getHttpHeaders(final String xApiKey, final String token) {
		return httpHeaders -> {
			httpHeaders.set("x-api-key", xApiKey);
			httpHeaders.setBearerAuth(token);
		};
	}
}
