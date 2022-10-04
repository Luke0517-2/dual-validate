package cht.bss.morder.dual.validate.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDiffTest {
	private ObjectMapper mapper = new ObjectMapper();

	private static String json1;

	private static String json2;

	private static String json3;

	@BeforeAll
	public static void beforeAll() throws IOException {
		json1 = FileUtils.readFileToString(new File("./jsonsample/sample1.json"), Charset.defaultCharset());
		json2 = FileUtils.readFileToString(new File("./jsonsample/sample2.json"), Charset.defaultCharset());
		json3 = FileUtils.readFileToString(new File("./jsonsample/sample3.json"), Charset.defaultCharset());
	}

	@Test
	public void testJsonIsEquals() throws Exception {
		JsonNode jsonFirst = mapper.readTree(json1);
		JsonNode jsonSecond = mapper.readTree(json2);

		assertEquals(jsonFirst, jsonSecond);
	}

	@Test
	public void testJsonIsNotEquals() throws Exception {
		JsonNode jsonFirst = mapper.readTree(json1);
		JsonNode jsonSecond = mapper.readTree(json3);

		assertNotEquals(jsonFirst, jsonSecond);
	}

}
