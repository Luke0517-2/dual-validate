package cht.bss.morder.dual.validate.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.enums.MoqueryContractType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithDateType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithMingGuoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryContractWithTwoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryOrderNoType;
import cht.bss.morder.dual.validate.enums.MoqueryRentCustNoType;
import cht.bss.morder.dual.validate.enums.MoquerySpsvcType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumWithDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumWithMingGuoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumWithTwoDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTelnumsWithDateType;
import cht.bss.morder.dual.validate.enums.MoqueryTranscashIdType;
import cht.bss.morder.dual.validate.enums.QrySalebehaviorType;
import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.TestCase;

@SpringBootTest(properties="dual-validate.checkTable=False")
public class QueryInputVerifyTest {

	private final static String TEST_TELNUM = "0933123456";
	private final static String TEST_CUSTID = "A123456";
	private final static String TEST_CONTRACT_ID = "4083556";
	private final static String TEST_DATE_YMD = "2022-12-27";
	private final static String TEST_MINGGUO_DATE_YM = "9809";

	@Value("${transfer.property.m-order.apigw-properties.emp-no}")
	private String testEmpNo;
	@Value("${transfer.property.m-order.apigw-properties.from-site}")
	private String testFromSite;
	@Value("${transfer.property.m-order.apigw-properties.client-ip}")
	private String testClientIp;	

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
			assertEquals(testEmpNo, queryInput.getEmpno());
			assertEquals(testFromSite, queryInput.getFromSite());
			assertEquals(testClientIp, queryInput.getClientip());
			assertEquals(TEST_TELNUM, queryInput.getParam().getTelnum());
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
			assertEquals(testEmpNo, queryInput.getEmpno());
			assertEquals(testFromSite, queryInput.getFromSite());
			assertEquals(testClientIp, queryInput.getClientip());
			assertNull(queryInput.getParam().getTelnum());
			assertEquals(TEST_CUSTID,queryInput.getParam().getCustid());
			assertEquals("custbehavior;",queryInput.getParam().getQuerydata());
	}
	
	@Test
	public void testGetComparedData_In_QrySaleBehavior() throws JsonProcessingException {
		TestCase testCase = getTestCase();
		ComparedData data = queryInputFactory.getComparedData(QrySalebehaviorType.qrySalebehavior, testCase);
		
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("qrysalebehavior", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals(TEST_TELNUM, queryInput.getParam().getTelnum());
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
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("contractret", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryOrderNoType() throws JsonProcessingException {
		TestCase case1 = TestCase.builder().orderno(TEST_CONTRACT_ID).build();

		MoqueryOrderNoType type = MoqueryOrderNoType.Modelinsrec;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);

		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("modelinsrec", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals(TEST_CONTRACT_ID, queryInput.getParam().getQueryitem().getContent());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractType() throws JsonProcessingException {
		TestCase case1 = TestCase.builder().contract(TEST_CONTRACT_ID).build();

		MoqueryContractType type = MoqueryContractType.AgentMobileSet;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);

		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("agentmobset", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals(TEST_CONTRACT_ID, queryInput.getParam().getQueryitem().getContent());
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
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
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
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("agent5id", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertEquals(TEST_TELNUM, queryInput.getParam().getQueryitem().getContent());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryContractWithDateType type = MoqueryContractWithDateType.Chgcustrec;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("chgcustrec", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithMingGuoDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryContractWithMingGuoDateType type = MoqueryContractWithMingGuoDateType.Officialfee;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("officialfee", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTwoDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryContractWithTwoDateType type = MoqueryContractWithTwoDateType.Applytypechgrec;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("applytypechgrec", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryRentCustNoType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryRentCustNoType type = MoqueryRentCustNoType.Pascustomer;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("pascustomer", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumsWithDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryTelnumsWithDateType type = MoqueryTelnumsWithDateType.Workingrecord;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("workingrecord", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryTelnumWithDateType type = MoqueryTelnumWithDateType.Adjustbill;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("adjustbill", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithMingGuoDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryTelnumWithMingGuoDateType type = MoqueryTelnumWithMingGuoDateType.Recotemp;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("recotemp", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithTwoDateType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryTelnumWithTwoDateType type = MoqueryTelnumWithTwoDateType.Empdiscntrec;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("empdiscntrec", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("2", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTranscashIdType() throws JsonProcessingException {
		TestCase case1 = getTestCase();

		MoqueryTranscashIdType type = MoqueryTranscashIdType.Chargeitem;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		String queryInputString = mapper.writeValueAsString(data.getQueryInput());
		QueryInput queryInput = mapper.readValue(queryInputString, QueryInput.class);
		
		assertEquals("moquery", queryInput.getCmd());
		assertEquals(testEmpNo, queryInput.getEmpno());
		assertEquals(testFromSite, queryInput.getFromSite());
		assertEquals(testClientIp, queryInput.getClientip());
		assertEquals("chargeitem", queryInput.getParam().getQueryitem().getTablename());
		assertEquals("1", queryInput.getParam().getQueryitem().getQuerytype());
		assertNull(queryInput.getParam().getTelnum());
		assertNull(queryInput.getParam().getQuerydata());
		assertNull(queryInput.getParam().getCustid());
	}
	
	private TestCase getTestCase() {
		// TODO Auto-generated method stub
		final String telnum = TEST_TELNUM;
		final String custid = TEST_CUSTID;
		TestCase case1 = TestCase.builder().telNum(telnum).custId(custid).build();
		return case1;
	}
}
