package cht.bss.morder.dual.validate.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
public class QueryInputFactoryTest {

	@Autowired
	private QueryInputFactory queryInputFactory;

	@Test
	public void testGetComparedData_In_QueryCustInfo_Telnum() {
		final String telnum = "0912345678";
		final String custid = "Helloworld";
		TestCase case1 = TestCase.builder().telNum(telnum).custId(custid).build();

		ComparedData data = queryInputFactory.getComparedData(QueryCustinfoType.telnum, case1);

		validataComparedData(data);
		assertEquals(data.getData(), telnum);
		assertEquals(data.getTable(), "telnum");
		assertEquals("querycustinfo", data.getQueryService());

		ComparedData data2 = queryInputFactory.getComparedData(QueryCustinfoType.custbehavior, case1);

		validataComparedData(data2);
		assertEquals(data2.getData(), custid);
		assertEquals(data2.getTable(), "custbehavior");
		assertEquals("querycustinfo", data.getQueryService());
	}

	@Test
	public void testGetComparedData_In_QrySaleBehavior() {
		final String telnum = "0912345678";
		final String custid = "Helloworld";
		TestCase case1 = TestCase.builder().telNum(telnum).custId(custid).build();

		ComparedData data = queryInputFactory.getComparedData(QrySalebehaviorType.qrySalebehavior, case1);

		validataComparedData(data);
		assertEquals(data.getData(), telnum);
		assertEquals(data.getTable(), "");
		assertEquals("qrysalebehavior", data.getQueryService());
	}

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTelnumType() {
		final String contractId = "Helloworld";
		final String telnum = "0912345678";
		TestCase case1 = TestCase.builder().telNum(telnum).contract(contractId).build();

		MoqueryContractWithTelnumType type = MoqueryContractWithTelnumType.Contractret;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		validataComparedData(data);
		validataComparedDataInMoquery(data, type);
		assertEquals(contractId + "&" + telnum, data.getData());
		assertEquals("moquery", data.getQueryService());
	}

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryOrderNoType() {
		final String orderNo = "Helloworld";
		TestCase case1 = TestCase.builder().orderno(orderNo).build();

		MoqueryOrderNoType type = MoqueryOrderNoType.Modelinsrec;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		validataComparedData(data);
		validataComparedDataInMoquery(data, type);
		assertEquals(orderNo, data.getData());
		assertEquals("moquery", data.getQueryService());
	}

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractType() {
		final String contractId = "Helloworld";
		TestCase case1 = TestCase.builder().contract(contractId).build();

		MoqueryContractType type = MoqueryContractType.AgentMobileSet;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		validataComparedData(data);
		validataComparedDataInMoquery(data, type);
		assertEquals(contractId, data.getData());
		assertEquals("moquery", data.getQueryService());
	}

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcType() {
		final String spsvc = "Helloworld";
		TestCase case1 = TestCase.builder().spsvc(spsvc).build();

		MoquerySpsvcType type = MoquerySpsvcType.Mdsvc;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		validataComparedData(data);
		validataComparedDataInMoquery(data, type);
		assertEquals(spsvc, data.getData());
		assertEquals("moquery", data.getQueryService());
	}

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumType() {
		final String telnum = "0912345678";
		TestCase case1 = TestCase.builder().telNum(telnum).build();

		MoqueryTelnumType type = MoqueryTelnumType.Agent5id;
		ComparedData data = queryInputFactory.getComparedData(type, case1);
		validataComparedData(data);
		validataComparedDataInMoquery(data, type);
		assertEquals(telnum, data.getData());
		assertEquals("moquery", data.getQueryService());
	}

	private void validataComparedDataInMoquery(ComparedData data, MoqueryEnumInterface type) {
		QueryInput input = data.getQueryInput();

		Params param = input.getParam();
		assertNotNull(param);

		QueryItem queryItem = param.getQueryitem();
		assertNotNull(queryItem);
		assertNotNull(queryItem.getTablename());
		assertEquals(type.getTableName(), queryItem.getTablename());
		assertNotNull(queryItem.getQuerytype());
		assertEquals(type.getType(), queryItem.getQuerytype());
		assertNotNull(queryItem.getContent());
	}

	private void validataComparedData(ComparedData data) {
		assertNotNull(data);
		assertNotNull(data.getQueryService());
		assertNotNull(data.getTable());
		assertNotNull(data.getData());

		QueryInput input = data.getQueryInput();
		assertNotNull(input);
		assertNotNull(input.getCmd());
		assertNotNull(input.getEmpno());
		assertNotNull(input.getFromSite());
		assertNotNull(input.getClientip());
		assertNotNull(input.getParam());
	}

}
