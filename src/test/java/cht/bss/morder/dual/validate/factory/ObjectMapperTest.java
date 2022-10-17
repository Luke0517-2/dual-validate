package cht.bss.morder.dual.validate.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.QueryItem;
import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest
public class ObjectMapperTest {

	@Autowired
	private QueryInputFactory queryInputFactory;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	public void testGetComparedData_In_QueryCustInfo_Telnum() throws JsonProcessingException {
		final String telnum = "0912345678";
		final String custid = "Helloworld";
		TestCase case1 = TestCase.builder().telNum(telnum).custId(custid).build();

		ComparedData data = queryInputFactory.getComparedData(QueryCustinfoType.telnum, case1);
		
			String queryInputString = mapper.writeValueAsString(data.getQueryInput());
			//readValue執行，param內皆為空值，待處理
			QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
			assertEquals("QueryCustInfo", queryInput.getCmd());
			assertEquals("123456", queryInput.getEmpno());
			assertEquals("EAI", queryInput.getFromSite());
			assertEquals("10.144.1.1", queryInput.getClientip());
			assertEquals("0912345678", queryInput.getParam().getTelnum());
			assertNull(queryInput.getParam().getCustid());
			assertEquals("telinfo;contractinfo;accountinfo;renter;user;simcard;package_all;spsvc_current;wap_current;promo_current;grouppromo_current;AWInfo;spsvcitem;sernum;vipdata;deposit;mcpsstore;basecontract;taxexempt;empdata;npinfo;CMPInfo;", 
						  queryInput.getParam().getTelnum());
			

	}
	

}
