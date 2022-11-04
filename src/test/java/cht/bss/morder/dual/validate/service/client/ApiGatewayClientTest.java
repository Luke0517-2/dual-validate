package cht.bss.morder.dual.validate.service.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;

import cht.bss.morder.dual.validate.config.ApiGateWayProperties;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:robert.lee@iisigroup.com">Robert Lee</a>
 *  <p> 
 **/
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApiGatewayClientTest {

	@Autowired
	private ApiGatewayClient apiGatewayClient;
	
	public static String apigwBaseUrl ="https://emssd.b2e.cht.com.tw:7715";


	@BeforeEach
	void setUp() throws Exception {
		log.info("You can use the below command to make sure");
		log.info("curl "+apigwBaseUrl +"/test");
		log.info("----------------------------------------------------");
	}
	

	@AfterEach
	void tearDown() throws Exception {
	}

	/***
	 * 查詢行動主檔：
	 * 
	 * <blockquote>
	 * 
	 * <pre>
	*     header - x-api-key: {x-api-key}
	*     header - Authorization: Bearer {access_token}
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * body : <blockquote>
	 * 
	 * <pre>
	 *    {
	 *     "param": {
	 *             "querydata": "spsvc_all;package_current;promo_current;telinfo;contractinfo;renter;user",
	 *             "telnum": "{telnum}"
	 *      },
	 *      "clientip": "{clientip}",
	 *      "cmd": "QueryCustInfo",
	 *      "empno": "{empno}",
	 *      "fromSite": "EAI"
	 *    }
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * 
	 **/
//	@Test
//	@Order(1) 
	void testGetApiDataForQueryCustInfo() throws Exception {
		
//		Map<String,String> params = new HashMap<String,String>();
//		params.put("querydata","spsvc_all;package_current;promo_current;telinfo;contractinfo;renter;user");
//		params.put("telnum","0911321571");
		Params params = Params.builder().querydata("spsvc_all;package_current;promo_current;telinfo;contractinfo;renter;user").telnum("0911321571").build();

		QueryInput queryRequest = QueryInput.builder()
				.param(params)
				.clientip("10.144.94.120")
				.cmd("QueryCustInfo")
				.empno("819446")
				.fromSite("EAI")
				.build();

		final String xApiKey = "64e2232b-683c-4467-9aec-ca6eb9c54236";
		final String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJxMTNicy1VZkcwUzNjYnZBOVZqcVgtS0NaY2JrRkhvM2VZLVRuVlk3S3hVIn0.eyJqdGkiOiI0OTNiYjIxZi1iNDllLTQ5MzUtOGQ4Yy1mNzY5MzI4YzlmNGEiLCJleHAiOjE2Njc1MzU2NzUsIm5iZiI6MCwiaWF0IjoxNjY3NTMyMDc1LCJpc3MiOiJodHRwczovL2Vtc3NkLmF1dGguY2h0LmNvbS50dy9hdXRoL3JlYWxtcy9DSFQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzhiZDE0ZjktOTJjOS00ZTU5LWEwZWMtMjhiMzRlZGMwMjNjIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiY29tLmNodC5pcG0udGwiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiJiNjczOWU2YS0yZDc4LTRmNzMtYjI1NS1jNmU4N2ZjNzRiMzQiLCJhY3IiOiIxIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iLCJhcGl1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiY29tLmNodC5pcG0udGwiOnsicm9sZXMiOlsidW1hX3Byb3RlY3Rpb24iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImNsaWVudElkIjoiY29tLmNodC5pcG0udGwiLCJjbGllbnRIb3N0IjoiMTAuMTMxLjIuMSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LWNvbS5jaHQuaXBtLnRsIiwiY2xpZW50QWRkcmVzcyI6IjEwLjEzMS4yLjEiLCJlbWFpbCI6InNlcnZpY2UtYWNjb3VudC1jb20uY2h0LmlwbS50bEBwbGFjZWhvbGRlci5vcmcifQ.fuB3Ken4e_m--0-_hc2pfqjirqWdOfk_qC8N75OB3Mz_YRwCYfec4cir0ocMy5RVBvpiFLd3nKCXFgH0DqaleAIE3KbzeIhBjFJVPtwtgqbnXAR6LeqovF4kokKaYJt5l5SIltSYfxIsF7DdBxCrFuA_TAwxK-f6GrUo0fSogGPDz0SAHJLN-kc9YCSFu3Hj-yfpbgpMR1e-x6TBLfPGRb76NixVsgbQQLOCLeyOpp16h0aPN9ZQiTjr1YImU2xFrbyBoKF57abgPzx539djFLRqA2jUD_lRyzJwT0NaK8zSbjXHWxP8NeTufKSOep04ai_tovEdr9BsMQ3TRyHp6w";

		//FIXME  通常apikey 是固定的，但是此次動態，所以保持彈性，留到測試進行確認
		final Consumer<HttpHeaders> ahttpHeaders = httpHeaders -> {
			httpHeaders.set("x-api-key", xApiKey);
			httpHeaders.setBearerAuth(token);
		};

		ApiGateWayProperties apigwproperties = mock(ApiGateWayProperties.class);
		when(apigwproperties.getTimeoutSec()).thenReturn(60);
		when(apigwproperties.getBaseUrl()).thenReturn(apigwBaseUrl);
		when(apigwproperties.getApigwUri()).thenReturn("/apis/CHT/MBMS-RestfulDispatcher-Dev/1.0.1/WSApServer/rest/RestfulDispatcher");

		Mono<String> data = apiGatewayClient.getApiDataByJson( ahttpHeaders, apigwproperties, queryRequest);
		assertNotNull( data.block());
	}



//	@Test
//	@Order(5)
//	void testParseToURI() {
//		ApiGateWayProperties apigwproperties = mock(ApiGateWayProperties.class);
//		when(apigwproperties.getBaseUrl()).thenReturn("https://apigw-qa.bss-tpe.cht.com.tw");
//		when(apigwproperties.getApigwUri()).thenReturn("/apis/CHT/CCBSSAPI/1.0/api/public");
//
//		ProductOfferingById request1 = new ProductOfferingById();
//		request1.setId(":id");
//		request1.setRdbOnly(false);
//		String actua1 = apiGatewayClient.parseToURI(apigwproperties, request1).toString();
//		Assertions.assertEquals("https://apigw-qa.bss-tpe.cht.com.tw/apis/CHT/CCBSSAPI/1.0/api/public/productoffering/allstatus/:id?rdbOnly=false", actua1);
//
//
//		ProductOfferingByOriginalkey request2 = new ProductOfferingByOriginalkey();
//		request2.setOfferingtype("promo");
//		request2.setOriginalkeys(new String[] {"101514","101515"});
//		String actua2 = apiGatewayClient.parseToURI(apigwproperties, request2).toString();
//		//cht npm (mw-api-se) container does not follow the URL-encoding rule 
//		assertEquals("https://apigw-qa.bss-tpe.cht.com.tw/apis/CHT/CCBSSAPI/1.0/api/public/productoffering/filter/byoriginalkey?offeringtype=promo&keys[]=101514,101515", actua2);
//
//		CodeTable request3 = new CodeTable();
//		request3.setTablename("applytype");
//		String actua3 = apiGatewayClient.parseToURI(apigwproperties, request3).toString();
//		assertEquals("https://apigw-qa.bss-tpe.cht.com.tw/apis/CHT/CCBSSAPI/1.0/api/public/codetable/tablename/applytype", actua3);
//	}


}
