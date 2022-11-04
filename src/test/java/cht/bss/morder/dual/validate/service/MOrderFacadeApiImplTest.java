package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(properties="dual-validate.mode=API")
public class MOrderFacadeApiImplTest {
	
	@Autowired
	private MOrderFacade facade;
	
	@Test
	public void testBelongToFileImpl() {
		
		assertTrue(facade instanceof MOrderFacadeApiImpl);
		
	}
	
//	@Test
	public void testQueryIISI() {
		Params params = Params.builder().querydata("spsvc_all;package_current;promo_current;telinfo;contractinfo;renter;user").telnum("0911321571").build();
		
 		QueryInput input = QueryInput.builder()
											.param(params)
											.clientip("10.144.94.120")
											.cmd("QueryCustInfo")
											.empno("819446")
											.fromSite("EAI")
											.build();
 		
		String result = facade.queryIISI(input);
		log.info("result:{}", result);
		assertNotNull(result);
	}
	
	private TestCase getTestCase() {
		return TestCase.builder().telNum("11223").custId("33445").build();
	}
}
