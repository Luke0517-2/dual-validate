package cht.bss.morder.dual.validate.adapter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.enums.MoqueryContractType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithMingGuoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryEnumForTwiceQuery;

@SpringBootTest
public class AdapterTest {

	@Autowired
	private FlexQueryTableAdapter adapter;
	
	
	@Test
	void initTest() {
		
	}
}
