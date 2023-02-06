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
public class OrderNoResponseVOTest {
	
	private static OrderNoResponseVO orderNo;
	private static OrderNoResponseVO emptyOrderNo;

	@BeforeAll
	static void init() throws IOException {
		String OrderNo_JSON = FileUtils.readFileToString(
				new File("./jsonsample/agentmobileset_output.json"), StandardCharsets.UTF_8);
		orderNo = OrderNoResponseVO.builder(OrderNo_JSON);
		String Empty_OrderNo_JSON = FileUtils.readFileToString(new File("./jsonsample/emptydatalist_output.json"),
				StandardCharsets.UTF_8);
		emptyOrderNo = OrderNoResponseVO.builder(Empty_OrderNo_JSON);
	}

	@Test
	void test_getContractID() {
		String result = orderNo.getOrderNo();
		assertEquals("M10TP3S037M26", result);
		
		String failResult = emptyOrderNo.getOrderNo();
		assertNull(failResult);
	}
}
