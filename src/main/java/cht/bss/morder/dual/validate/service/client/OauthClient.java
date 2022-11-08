package cht.bss.morder.dual.validate.service.client;

import java.time.Duration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import cht.bss.morder.dual.validate.common.exceptions.CommonHandlingException;
import cht.bss.morder.dual.validate.config.OAuthProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
 
/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *  <p>
 *  <code>https://github.com/resilience4j/resilience4j-spring-boot2-demo </code>
 *  <code>https://resilience4j.readme.io/v1.6.0/docs/getting-started-3  </code>
 *  <code>https://www.oauth.com/oauth2-servers/device-flow/token-request </code>
 * */
@Slf4j
@Service
public class OauthClient {

	/** The handler. */
	@Autowired
	private CommonHandlingException handler;
	
	/** The web client. */
	private WebClient webClient;
	
//	/**
//	 * for create a real SSL Context in production
//	 * @return
//	 */
//	private SslContext createSSLContext() {
//		try {
//			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			keyStore.load(getClass().getClassLoader().getResourceAsStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());
//
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
//			keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
//
//			return SslContextBuilder.forClient().keyManager(keyManagerFactory).build();
//
//		} catch (Exception e) {
//			throw new RuntimeException("Error creating SSL context.");
//		}
//	}
	
	private SslContext createSSLContext() throws SSLException {
		return SslContextBuilder
	            .forClient()
	            .trustManager(InsecureTrustManagerFactory.INSTANCE)
	            .build();
	}

	/**
	 * Post construct.
	 * @throws SSLException 
	 */
	@PostConstruct
	protected void postConstruct(){
		
		HttpClient httpClient = HttpClient.create().secure(t -> {
			try {
				t.sslContext(createSSLContext());
			} catch (SSLException e) {
				log.error("Create SSL Context fail:{}", e.getMessage());
			}
		});
		
		this.webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient)).clone().build();
	}

	/**
	 * Get the token.
	 *
	 * @param oAuthProperties
	 * @return
	 */
	public Mono<String> getToken(final OAuthProperties oAuthProperties) {
		final String clientId = oAuthProperties.getClientId();
		final String clientSecret = oAuthProperties.getClientSecret();
		final String tokenUrl = oAuthProperties.getTokenUrl();
		final int timeoutSec = oAuthProperties.getTimeoutSec();

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("client_id", clientId);
		formData.add("client_secret", clientSecret);
		formData.add("grant_type", "client_credentials");

		log.info("requestBody: {}", formData);

		final Mono<String> mono = webClient.post().uri(tokenUrl)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData(formData))
			    .retrieve()			    
			    .bodyToMono(String.class).log().onErrorMap(WebClientResponseException.class, ex -> handler.handleException(ex))
				.timeout(Duration.ofSeconds(timeoutSec));
						
		return mono;
	}

}
