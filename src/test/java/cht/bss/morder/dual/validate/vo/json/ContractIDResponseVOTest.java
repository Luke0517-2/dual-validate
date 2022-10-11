package cht.bss.morder.dual.validate.vo.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContractIDResponseVOTest {

	private static ContractIDResponseVO contractID;
	private static ContractIDResponseVO emptyContractID;
	
	@BeforeAll
	static void init() throws IOException {
		String ContractID_JSON = FileUtils.readFileToString(new File("./jsonsample/numberusage_output.json"),
				StandardCharsets.UTF_8);
		contractID = ContractIDResponseVO.builder(ContractID_JSON);
		String Empty_ContractID_JSON = FileUtils.readFileToString(new File("./jsonsample/emptydatalist_output.json"),
				StandardCharsets.UTF_8);
		emptyContractID = ContractIDResponseVO.builder(Empty_ContractID_JSON);
	}
	
	@Test
	void test_getContractID() {
		String result = contractID.getContractID();
		assertEquals("542393", result);
		
		String failResult = emptyContractID.getContractID();
		assertNull(failResult);
	}
}
