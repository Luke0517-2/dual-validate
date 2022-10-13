package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.service.query.QrysalebehaviorService;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest
public class QrysalebehaviorServiceTest {

	@Autowired
	private QrysalebehaviorService service;

	@Test
	void testQueryData() {
		TestCase testCase = getTestCase();
		List<ComparedData> result = service.queryTestCase(testCase);

		assertNotNull(result);
		assertNotEquals(0, result);
		assertEquals(1, result.size());
	}

	private TestCase getTestCase() {
		return TestCase.builder().telNum("11223").custId("33445").build();
	}

}
