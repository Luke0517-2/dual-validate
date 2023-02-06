package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.service.query.MoqueryService;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest(properties = {"dual-validate.checkTable=True"})
public class MoqueryServiceTest {

	@Autowired
	private MoqueryService service;

	@Test
	void testQueryData() {
		TestCase testCase = getTestCase();
		List<ComparedData> result = service.queryTestCase(testCase);

		assertNotNull(result);
		assertNotEquals(0, result);

		// 17 (2022-11 雙軌階段一)
		// +42 (2023-01 雙軌階段二)
		// -1 (2023-02 test properties 拿掉numberusage)
		assertEquals(58, result.size());
	}



	private TestCase getTestCase() {
		return TestCase.builder().telNum("11223").custId("33445").build();
	}
	
	@Test
	void testmergeQuerys() {
		QueryInput queryInput = mock(QueryInput.class);
		ComparedData comparedDataFromCht = ComparedData.builder().queryInput(queryInput).dataFromCht("fromCht").dataFromIISI(null).data("data").queryService("moquery").build();
		ComparedData comparedDataFromIISI = ComparedData.builder().queryInput(queryInput).dataFromCht(null).dataFromIISI("fromIISI").data("data").queryService("moquery").build();
		
		MoqueryServiceJUnit moqueryServiceJUnit = new MoqueryServiceJUnit();
		ComparedData mergedData = moqueryServiceJUnit.mergeQuerysTesting(comparedDataFromCht, comparedDataFromIISI);
		
		assertNotNull(mergedData.getDataFromCht());
		assertNotNull(mergedData.getDataFromIISI());
	}
	
	/**
	 * For testing protected method in MoqueryService Class
	 *
	 */
	private class MoqueryServiceJUnit extends MoqueryService {
		
		public ComparedData mergeQuerysTesting(ComparedData comparedForCht, ComparedData comparedForIISI) {
			return mergeQuerys(comparedForCht, comparedForIISI);
		}
	}

}
