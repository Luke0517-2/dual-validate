package cht.bss.morder.dual.validate.vo.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContractIDTest {

	private static ContractID contractID;
	
	@BeforeAll
	static void init() throws IOException {
		String ContractID_JSON = FileUtils.readFileToString(new File("./jsonsample/contractID.json"),
				StandardCharsets.UTF_8);
		contractID = ContractID.builder(ContractID_JSON);
	}
	
	@Test
	void test_getContractID() {
		String result = contractID.getContractID();
		assertEquals("542393", result);
	}
}
