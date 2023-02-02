package cht.bss.morder.dual.validate.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.common.YesterdayConvert;

@SpringBootTest
public class YesterdayConvertTest {

	@Autowired
	private YesterdayConvert yesterdayConvert;
	
	@Test
	void test_checkInstance() {
		assertNotNull(yesterdayConvert);
	}
	
	private final String yesterday = LocalDate.now().minusDays(1).toString();

    private static String getMingGouString() {
        int intYear = LocalDate.now().minusDays(1).getYear() - 1911;
        int intMonth = LocalDate.now().minusDays(1).getMonthValue();
        String month;
        if (intMonth < 10) {
            month = "0" + intMonth;
        } else
            month = String.valueOf(intMonth);
        return intYear + month;
    }


	@Test
	void test_getYesterdayADDateString() {
		
		String yesterdayADDateString = yesterdayConvert.getYesterdayADDateString();	
		assertEquals(yesterday, yesterdayADDateString);
	}
	
	@Test
	void test_getYesterdayMinguoDate() {
		
		String yesterdayMinguoDate = yesterdayConvert.getYesterdayMinguoDate();
		assertEquals(getMingGouString(),yesterdayMinguoDate);
	}
}
