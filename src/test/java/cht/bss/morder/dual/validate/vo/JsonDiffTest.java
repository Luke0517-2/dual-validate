package cht.bss.morder.dual.validate.vo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import cht.bss.morder.dual.validate.enums.CompareResultType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDiffTest {
	private ObjectMapper mapper = new ObjectMapper();

	private static String json1;

	private static String json2;

	private static String json3;

	private static String jsonArray1;
	private static String jsonArray2;

	private static String testCase1;
	private static String testCase2;
	private final static String jsonStructureError = "{\"BMS\":{\"Status\":\"0\",\"Msg\":\"失敗\",\"CustID\":\"G499280561\",\"Data\":[{\"notsmsbill\":\"N\"";

	@BeforeAll
	public static void beforeAll() throws IOException {
		json1 = FileUtils.readFileToString(new File("./jsonsample/sample1.json"), Charset.defaultCharset());
		json2 = FileUtils.readFileToString(new File("./jsonsample/sample2.json"), Charset.defaultCharset());
		json3 = FileUtils.readFileToString(new File("./jsonsample/sample3.json"), Charset.defaultCharset());
		jsonArray1 = FileUtils.readFileToString(new File("./jsonsample/json_array_1.json"), Charset.defaultCharset());
		jsonArray2 = FileUtils.readFileToString(new File("./jsonsample/json_array_2.json"), Charset.defaultCharset());
		testCase1 = FileUtils.readFileToString(new File("./jsonsample/CHT/telnum_0933121XXX_CHT"), Charset.defaultCharset());
		testCase2 = FileUtils.readFileToString(new File("./jsonsample/IISI/telnum_0933121XXX_IISI"), Charset.defaultCharset());
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
	
	@Test
	public void testStringUtilsEquals() {
		boolean restult = StringUtils.equals(json1, json2);
		
		assertEquals(false, restult);
	}

	@Test
	public void testArrayEquals() throws JsonProcessingException {
		ComparedData comparedData = ComparedData.builder().dataFromCht(jsonArray1).dataFromIISI(jsonArray2).build();
		CompareResultType comparedResult = comparedData.getComparedResult();
		assertEquals(CompareResultType.EQUAL,comparedResult);
	}

	@Test
	public void testJSONException() {
		ComparedData comparedData = ComparedData.builder().dataFromCht(jsonStructureError).dataFromIISI(testCase2).build();
		try {
			comparedData.compareJsonIgnoreOrder();
		}catch (JSONException e){
			System.out.println("Exception happen : " + e.getMessage());
		}
	}

	@Test
	public void testJSONAssert(){
		ComparedData comparedData = ComparedData.builder().dataFromCht(testCase1).dataFromIISI(testCase2).build();
		comparedData.compareJsonIgnoreOrder();
	}
}
