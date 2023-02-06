package cht.bss.morder.dual.validate.vo.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpecsvcIDResponseVOTest {
	private static SpecsvcIDResponseVO specsvcID;
	private static SpecsvcIDResponseVO emptySpecsvcID;

	@BeforeAll
	static void init() throws IOException {
		String SpecsvcID_JSON = FileUtils.readFileToString(
				new File("./jsonsample/SpecsvcidMN_output.json"), StandardCharsets.UTF_8);
		specsvcID = SpecsvcIDResponseVO.builder(SpecsvcID_JSON);
		String Empty_SpecsvcID_JSON = FileUtils.readFileToString(new File("./jsonsample/emptydatalist_output.json"),
				StandardCharsets.UTF_8);
		emptySpecsvcID = SpecsvcIDResponseVO.builder(Empty_SpecsvcID_JSON);
	}

	@Test
	void test_getContractID() {
		String result = specsvcID.getSpecsvcID();
		assertEquals("226226700", result);
		
		String failResult = emptySpecsvcID.getSpecsvcID();
		assertNull(failResult);
	}
}
