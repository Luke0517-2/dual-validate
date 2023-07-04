package cht.bss.morder.dual.validate.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DaysConvertTest {

	@Autowired
	private DaysConvert daysConvert;
	private int convertDay;

	@BeforeEach
	public void setConvertDay(){
		convertDay = -(daysConvert.getDay());
	}

	@Test
	void test_checkInstance() {
		assertNotNull(daysConvert);
	}

    private String getMingGouString() {
        int intYear = LocalDate.now().minusDays(convertDay).getYear() - 1911;
        int intMonth = LocalDate.now().minusDays(convertDay).getMonthValue();
        String month;
        if (intMonth < 10) {
            month = "0" + intMonth;
        } else
            month = String.valueOf(intMonth);
        return intYear + month;
    }


	@Test
	void test_getYesterdayADDateString() {
		String beforeDayADDateString = daysConvert.getConvertADDate();
		String beforeDay = LocalDate.now().minusDays(convertDay).toString();
		assertEquals(beforeDay, beforeDayADDateString);
	}
	
	@Test
	void test_getYesterdayMinguoDate() {
		String beforeDayMinguoDate = daysConvert.getConvertMinguoDate();
		assertEquals(getMingGouString(),beforeDayMinguoDate);
	}
}
