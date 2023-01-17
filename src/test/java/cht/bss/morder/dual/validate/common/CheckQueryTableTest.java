package cht.bss.morder.dual.validate.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CheckQueryTableTest {

	@Value("#{'${dual-validate-query-table}'.split(',')}")
	private List<String> listOfQueryTable;
	
	@Value("#{${dual-validate-query-table-list}}")
	private List<String> listOfQueryTableV2;
	
	@Autowired(required = false)
	private CheckQueryTable checkQueryTable;
	
	@Test
	void test_getValueFromProperties() {
		assertEquals(Arrays.asList("adjustbill", "recotemp"), listOfQueryTable);
	}
	
	@Test
	void test_getValueFromPropertiesV2() {
		assertEquals(Arrays.asList("adjustbill", "recotemp"), listOfQueryTable);
		
	}
	
	@Test
	void test_filterQueryTable() {
		boolean resultForTrue = checkQueryTable.filterQueryTable("adjustbill");
		assertTrue(resultForTrue);
		boolean resultForFalse = checkQueryTable.filterQueryTable("refund");
		assertFalse(resultForFalse);
	}
	
	@Test
	void test_conditionCreateInstance() {
		assertNull(checkQueryTable);
	}
}
