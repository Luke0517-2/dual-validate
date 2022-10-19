package cht.bss.morder.dual.validate.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.enums.MoqueryContractType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryEnumInterface;
import cht.bss.morder.dual.validate.enums.MoqueryOrderNoType;
import cht.bss.morder.dual.validate.enums.MoquerySpsvcType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumType;
import cht.bss.morder.dual.validate.enums.QrySalebehaviorType;
import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.QueryItem;
import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest
public class QueryInputVerifyTest {

	@Autowired
	private QueryInputFactory queryInputFactory;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Test
	public void testGetComparedData_In_QueryCustInfo_Telnum() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		ComparedData data = queryInputFactory.getComparedData(QueryCustinfoType.telnum, case1);
			String queryInputString = mapper.writeValueAsString(data.getQueryInput());
			QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
			
			assertEquals("QueryCustInfo", queryInput.getCmd());
			assertEquals("123456", queryInput.getEmpno());
			assertEquals("EAI", queryInput.getFromSite());
			assertEquals("10.144.1.1", queryInput.getClientip());
			assertEquals("0912345678", queryInput.getParam().getTelnum());
			assertNull(queryInput.getParam().getCustid());
			assertEquals("telinfo;contractinfo;accountinfo;renter;user;simcard;package_all;spsvc_current;wap_current;promo_current;grouppromo_current;AWInfo;spsvcitem;sernum;vipdata;deposit;mcpsstore;basecontract;taxexempt;empdata;npinfo;CMPInfo;", 
						  queryInput.getParam().getQuerydata());
			
	}
	
	@Test
	public void testGetComparedData_In_QueryCustInfo_CustID() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		ComparedData data = queryInputFactory.getComparedData(QueryCustinfoType.custbehavior, case1);
			String queryInputString = mapper.writeValueAsString(data.getQueryInput());
			QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
			
			assertEquals("QueryCustInfo", queryInput.getCmd());
			assertEquals("123456", queryInput.getEmpno());
			assertEquals("EAI", queryInput.getFromSite());
			assertEquals("10.144.1.1", queryInput.getClientip());
			assertNull(queryInput.getParam().getTelnum());
			assertEquals("Helloworld",queryInput.getParam().getCustid());
			assertEquals("custbehavior;",queryInput.getParam().getQuerydata());
			
	}
	
	@Test
	public void testGetComparedData_In_QrySaleBehavior() throws JsonProcessingException {
		TestCase testCase = getTestCase();
		ComparedData data = queryInputFactory.getComparedData(QrySalebehaviorType.qrySalebehavior, testCase);
		
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("qrysalebehavior", queryInput.getCmd());
		assertEquals("123456", queryInput.getEmpno());
		assertEquals("EAI", queryInput.getFromSite());
		assertEquals("10.144.1.1", queryInput.getClientip());
		assertEquals("0912345678", queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
		
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTelnumType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryContractWithTelnumType type = MoqueryContractWithTelnumType.Contractret;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals("123456", queryInput.getEmpno());
		assertEquals("EAI", queryInput.getFromSite());
		assertEquals("10.144.1.1", queryInput.getClientip());
		assertEquals("contractret", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryOrderNoType() throws JsonProcessingException {
		final String orderNo = "Helloworld";
		TestCase case1 = TestCase.builder().orderno(orderNo).build();

		MoqueryOrderNoType type = MoqueryOrderNoType.Modelinsrec;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);

		assertEquals("moquery", queryInput.getCmd());
		assertEquals("123456", queryInput.getEmpno());
		assertEquals("EAI", queryInput.getFromSite());
		assertEquals("10.144.1.1", queryInput.getClientip());
		assertEquals("modelinsrec", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals("Helloworld", queryInput.getParam().getQueryitem().getContent());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractType() throws JsonProcessingException {
		final String contractId = "Helloworld";
		TestCase case1 = TestCase.builder().contract(contractId).build();

		MoqueryContractType type = MoqueryContractType.AgentMobileSet;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);

		assertEquals("moquery", queryInput.getCmd());
		assertEquals("123456", queryInput.getEmpno());
		assertEquals("EAI", queryInput.getFromSite());
		assertEquals("10.144.1.1", queryInput.getClientip());
		assertEquals("agentmobset", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals("Helloworld", queryInput.getParam().getQueryitem().getContent());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
		
	}
	
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcType() throws JsonMappingException, JsonProcessingException {
		final String spsvc = "Helloworld";
		TestCase case1 = TestCase.builder().spsvc(spsvc).build();

		MoquerySpsvcType type = MoquerySpsvcType.Mdsvc;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);

		assertEquals("moquery", queryInput.getCmd());
		assertEquals("123456", queryInput.getEmpno());
		assertEquals("EAI", queryInput.getFromSite());
		assertEquals("10.144.1.1", queryInput.getClientip());
		assertEquals("mdsvc", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals("Helloworld", queryInput.getParam().getQueryitem().getContent());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryTelnumType type = MoqueryTelnumType.Agent5id;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals("123456", queryInput.getEmpno());
		assertEquals("EAI", queryInput.getFromSite());
		assertEquals("10.144.1.1", queryInput.getClientip());
		assertEquals("agent5id", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals("0912345678", queryInput.getParam().getQueryitem().getContent());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	private TestCase getTestCase() {
		// TODO Auto-generated method stub
		final String telnum = "0912345678";
		final String custid = "Helloworld";
		TestCase case1 = TestCase.builder().telNum(telnum).custId(custid).build();
		return case1;
	}
}
