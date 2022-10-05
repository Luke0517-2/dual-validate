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
public class OrderNoTest {
	
	private static OrderNo orderNo;

	@BeforeAll
	static void init() throws IOException {
		String OrderNo_JSON = FileUtils.readFileToString(
				new File("./jsonsample/orderNo.json"), StandardCharsets.UTF_8);
		orderNo = OrderNo.builder(OrderNo_JSON);
	}

	@Test
	void test_getContractID() {
		String result = orderNo.getOrderNo();
		assertEquals("M10TP3S037M26", result);
	}
}
