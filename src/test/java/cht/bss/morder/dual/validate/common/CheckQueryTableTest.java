package cht.bss.morder.dual.validate.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties="dual-validate.checkTable=True")
public class CheckQueryTableTest {

	
	@Value("#{${dual-validate-query-table-list}}")
	private List<String> listOfQueryTable;
	
	@Autowired(required = false)
	private CheckQueryTable checkQueryTable;
		
	
	@Test
	void test_filterQueryTable() {
		boolean resultForTrue = checkQueryTable.filterQueryTable("adjustbill");
		assertTrue(resultForTrue);
	}
	
	@Test
	void test_conditionCreateInstance() {
		assertNotNull(checkQueryTable);
	}
}
