package cht.bss.morder.dual.validate.vo.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.time.YesterdayConvert;

@SpringBootTest
public class YesterdayConvertTest {

	@Autowired
	private YesterdayConvert yesterdayConvert;
	
	@Test
	void test_checkInstance() {
		assertNotNull(yesterdayConvert);
	}

	@Test
	void test_getYesterdayADDateString() {
		
		String yesterdayADDateString = yesterdayConvert.getYesterdayADDateString();	
		assertEquals("2023-01-16", yesterdayADDateString);
	}
	
	@Test
	void test_getYesterdayMinguoDate() {
		
		String yesterdayMinguoDate = yesterdayConvert.getYesterdayMinguoDate();
		assertEquals("11201",yesterdayMinguoDate);
	}
}
