package cht.bss.morder.dual.validate.service.client;

import java.net.URI;
import java.time.Duration;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

//import cht.bss.apigw.vo.*;
import cht.bss.morder.dual.validate.common.exceptions.CommonHandlingException;
import cht.bss.morder.dual.validate.config.ApiGateWayProperties;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *  <p>
 *  <code>https://github.com/resilience4j/resilience4j-spring-boot2-demo </code>
 *  <code>https://resilience4j.readme.io/v1.6.0/docs/getting-started-3  </code>
 *  <code>https://www.oauth.com/oauth2-servers/device-flow/token-request </code>
 * */
@Slf4j
@Service
public class ApiGatewayClient {
	
	/** The handler. */
	@Autowired
	private CommonHandlingException handler;
	
//	@Resource
//	private IApigwClient self ;

	/** The web client. */
	private WebClient webClient;

	/**
	 * Post construct.
	 */
	@PostConstruct
	protected void postConstruct() {
		//索取資料如果過於龐大，需要擴大szie
		ExchangeStrategies strategies =
				ExchangeStrategies.builder()
						.codecs(codecs ->
								codecs.defaultCodecs().maxInMemorySize(4 * 1024 * 1024))
						.build();

		this.webClient = WebClient.builder().clone().exchangeStrategies(strategies).build(); 
	}

	/**
	 * Gets the api data by json.
	 * <p> https://www.elastic.co/guide/en/apm/agent/java/master/method-api.html
	 * @param httpHeaders the http headers
	 * @param apigwProperties the properties
	 * @param body the body
	 * @return the api data by json
	 */ 
	public Mono<String> getApiDataByJson(final Consumer<HttpHeaders> httpHeaders, final ApiGateWayProperties apigwProperties, final Object body) {		
		int timeoutSec = apigwProperties.getTimeoutSec();
		
		final Class<?> requsetBodyclassType = body.getClass();
		final String queryURL = this.parseToURI(apigwProperties ); 
		
		final Mono<String> result = webClient.post().uri(queryURL).contentType(MediaType.APPLICATION_JSON).headers(httpHeaders)
				.body(Mono.just(body), requsetBodyclassType).retrieve().bodyToMono(String.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handler.handleException(ex))
				.timeout(Duration.ofSeconds(timeoutSec));
		 return result ;
	} 
	 
//	/**
//	 * Gets the api data by form.
//	 * 根據2021.0930與中華會議決定暫時使用源系統，故此方法暫無使用
//	 * <p> https://www.elastic.co/guide/en/apm/agent/java/master/method-api.html
//	 * @param httpHeaders the http headers
//	 * @param apigwProperties the properties
//	 * @param body 固網的請求參數
//	 * @return the api data by form
//	 */ 
//	public Mono<String> getApiDataByForm(final Consumer<HttpHeaders> httpHeaders, final ApiGateWayProperties apigwProperties, final FOrderRequest body) {		
//		int timeoutSec = apigwProperties.getTimeoutSec(); 
//		
//		final MultiValueMap<String, String> formData = body.toBeFormData();		
//		final String queryURL = this.parseToURI(apigwProperties ); 
//		
//		final Mono<String> mono = webClient.post().uri(queryURL).contentType(MediaType.APPLICATION_FORM_URLENCODED).headers(httpHeaders)
//				.body(BodyInserters.fromFormData(formData)).retrieve().bodyToMono(String.class).log()
//				.onErrorMap(WebClientResponseException.class, ex -> handler.handleException(ex))
//				.timeout(Duration.ofSeconds(timeoutSec));
//						
//		return mono;
//	} 
	
//	/**
//	 * Gets the api data by get.
//	* <p> https://www.elastic.co/guide/en/apm/agent/java/master/method-api.html
//	 * @param httpHeaders the http headers
//	 * @param apigwProperties the properties
//	 * @param request NPM源系統的請求
//	 * @return the api data by get
//	 */ 
//	public Mono<String> getApiDataByGet(final Consumer<HttpHeaders> httpHeaders, final ApiGateWayProperties apigwProperties, final NPMRequest request) {
//		int timeoutSec = apigwProperties.getTimeoutSec();
//
//		final URI queryURL = this.parseToURI(apigwProperties, request); 
//		   
//		final Mono<String> mono = webClient.get().uri(queryURL).headers(httpHeaders).retrieve().bodyToMono(String.class).log()
//				.onErrorMap(WebClientResponseException.class, ex -> handler.handleException(ex))
//				.timeout(Duration.ofSeconds(timeoutSec));
//						
//		return mono;
//	}

//	/**
//	 * 根據2021.0930與中華會議決定暫時使用源系統
//	 * <p> https://www.elastic.co/guide/en/apm/agent/java/master/method-api.html
//	 * @deprecated
//	 * @param apigwProperties apigwProperties
//	 * @param request 固網的請求參數
//	 * @return
//	 */ 
//	public Mono<String> getApiDataByGetFOrderRequest(final ApiGateWayProperties apigwProperties, final FOrderRequest request) {
//		
//		int timeoutSec = apigwProperties.getTimeoutSec();
//
//		final String queryURL = this.parseToURI(apigwProperties, request); 
//		final Mono<String>mono = webClient.get().uri(queryURL).retrieve().bodyToMono(String.class).log()
//				.onErrorMap(WebClientResponseException.class, ex -> handler.handleException(ex))
//				.timeout(Duration.ofSeconds(timeoutSec));
//		return mono;
//	}

//	/**
//	 * 透過固網的請求參數，來產生GET URL Path
//	 *
//	 * @param apigwProperties
//	 * @param request 固網的請求參數
//	 * @return
//	 */ 
//	public String parseToURI(final ApiGateWayProperties apigwProperties, final FOrderRequest request) {
//		final String baseUrl = apigwProperties.getBaseUrl();
//		final String apiUri = apigwProperties.getApigwUri();
// 
//		final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
//		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//		
//		builder.path(apiUri);
//		params.add("command", request.getCommand());
//		params.add("param", request.getParam());
//		
//		if(StringUtils.isNotEmpty( request.getEmpNo())) {
//			params.add("empNo", request.getEmpNo());
//		}
//		
//		if(StringUtils.isNotEmpty(request.getFromSite())) {
//			params.add("fromSite", request.getFromSite());
//		}
//
//		if(StringUtils.isNotEmpty(request.getClientIp())) {
//			params.add("clientIp", request.getClientIp());
//		}
//		builder.queryParams(params);
//		String result = builder.build().encode().toUri().toString();
//		return result;
//	}
	/**
	 * 透過固網的請求參數，來產生GET URL Path
	 *
	 * @param apigwProperties 
	 * @return
	 */ 
	public String parseToURI (final ApiGateWayProperties apigwProperties ) {
		final String baseUrl = apigwProperties.getBaseUrl();
		final String apiUri = apigwProperties.getApigwUri();

		final String url = String.format("%s%s", baseUrl, apiUri);
		return url;
	}
//	/**
//	 * 透過NPM源系統的請求參數，來產生GET URL Path
//	 *
//	 * @param apigwProperties
//	 * @param request NPM源系統的請求
//	 * @return
//	 */ 
//	public URI parseToURI(final ApiGateWayProperties apigwProperties, final NPMRequest request) {
//		String baseUrl = apigwProperties.getBaseUrl();
//		String apiUri = apigwProperties.getApigwUri();
//
//		final String url = String.format("%s%s", baseUrl, apiUri);
//		final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
//		final MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
//
//		if (request instanceof ProductOfferingById) {
//			ProductOfferingById data = (ProductOfferingById)request ;
//			builder.path(request.resourcePath()).path(data.getId());
//
//			if(data.getRdbOnly() != null ) {
//				params.add("rdbOnly", data.getRdbOnly().toString());
//			}
//
//		} else if (request instanceof CodeTable) {
//			CodeTable data = (CodeTable)request ;
//			builder.path(request.resourcePath()).path(data.getTablename());
//			
//			if(data.getParams() != null && data.getParams().size() > 0) {
//				data.getParams().forEach((k, v) -> params.add(k, v));
//			}
//
//		} else if (request instanceof ProductOfferingByOriginalkey) {
//			ProductOfferingByOriginalkey data = (ProductOfferingByOriginalkey)request;
//			builder.path(request.resourcePath());
//
//			if(data.getOfferingtype() != null ) {
//				params.add("offeringtype", data.getOfferingtype());
//			}
//
//			final String[] originalkeys = data.getOriginalkeys() ;
//			if (originalkeys != null && originalkeys.length != 0) {				
//				params.add("keys[]", String.join(",", originalkeys)); 
//			}
//			
//		}
//
//		builder.queryParams(params);  
//		//cht npm (mw-api-se) container does not follow the URL-encoding rule 
////		final URI result = builder.build().encode().toUri() ;
//		final URI result = builder.build().toUri() ;
//		return result ;
//	}
}
