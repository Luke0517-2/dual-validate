package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest(properties="dual-validate.mode=API")
public class MOrderFacadeApiImplTest {
	
	@Autowired
	private MOrderFacade facade;
	
	@Test
	public void testBelongToFileImpl() {
		
		assertTrue(facade instanceof MOrderFacadeApiImpl);
		
	}
	
	@Test
	public void testQueryIISI() {
		
	}
	
	private TestCase getTestCase() {
		return TestCase.builder().telNum("11223").custId("33445").build();
	}
}
