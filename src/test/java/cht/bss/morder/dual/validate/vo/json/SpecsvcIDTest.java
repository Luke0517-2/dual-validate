package cht.bss.morder.dual.validate.vo.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpecsvcIDTest {
	private static SpecsvcID specsvcID;

	@BeforeAll
	static void init() throws IOException {
		String SpecsvcID_JSON = FileUtils.readFileToString(
				new File("./jsonsample/specsvcID.json"), StandardCharsets.UTF_8);
		specsvcID = SpecsvcID.builder(SpecsvcID_JSON);
	}

	@Test
	void test_getContractID() {
		String result = specsvcID.getSpecsvcID();
		assertEquals("226226700", result);
	}
}
