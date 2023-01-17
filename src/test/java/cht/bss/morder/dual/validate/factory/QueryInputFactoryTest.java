package cht.bss.morder.dual.validate.factory;

import cht.bss.morder.dual.validate.enums.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.QueryItem;
import cht.bss.morder.dual.validate.vo.TestCase;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QueryInputFactoryTest {

    @Autowired
    private QueryInputFactory queryInputFactory;

    private final String yesterday = LocalDate.now().minusDays(1).toString();

    private static String getMingGouString() {
        int intYear = LocalDate.now().minusDays(1).getYear() - 1911;
        int intMonth = LocalDate.now().minusDays(1).getMonthValue();
        String month;
        if (intMonth < 10) {
            month = "0" + intMonth;
        } else
            month = String.valueOf(intMonth);
        return intYear + month;
    }

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
		String telnumFromComparedData = getTelnumFromComparedData(data);
		assertFalse(StringUtils.isEmpty(telnumFromComparedData));
		assertEquals(telnum,telnumFromComparedData);
		
		validataComparedData(data);
		assertEquals(telnum, data.getData());
		assertEquals("qrysalebehavior", data.getTable());
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
    public void testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcTypeCase2() {
        final String spsvc = "Helloworld";
        TestCase case1 = TestCase.builder().spsvc(spsvc).build();

        MoquerySpsvcType type = MoquerySpsvcType.F3svc;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(spsvc + "&" + yesterday + "&" + yesterday, data.getData());
        assertEquals("f3svc", data.getQueryInput().getParam().getQueryitem().getTablename());
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

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithDateType type = MoqueryContractWithDateType.Telsusptype;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(contractId + "&" + yesterday, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("telsusptype", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithMingGuoDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithMingGuoDateType type = MoqueryContractWithMingGuoDateType.Officialfee;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(contractId + "&" + getMingGouString(), data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("officialfee", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTwoDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithTwoDateType type = MoqueryContractWithTwoDateType.Discnttype;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(contractId + "&" + yesterday + "&" + yesterday, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("discnttype", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithDateType type = MoqueryTelnumWithDateType.ModelinsrecShop;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(telnum + "&" + yesterday, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("modelinsrec_shop", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithMingGuoDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithMingGuoDateType type = MoqueryTelnumWithMingGuoDateType.Recotemp;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(telnum + "&" + getMingGouString(), data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("recotemp", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithTwoDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithTwoDateType type = MoqueryTelnumWithTwoDateType.Empdiscntrec;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(telnum + "&" + yesterday + "&" + yesterday, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("empdiscntrec", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumsWithDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumsWithDateType type = MoqueryTelnumsWithDateType.Workingrecord;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(yesterday + "&" + telnum + "&" + telnum, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("workingrecord", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryRentCustNoType() {
        final String rentcustno = "123456789";
        TestCase case1 = TestCase.builder().rentcustno(rentcustno).build();

        MoqueryRentCustNoType type = MoqueryRentCustNoType.Pascustomer;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(rentcustno, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("pascustomer", data.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTranscashIdType() {
        final String transcashId = "123456789";
        TestCase case1 = TestCase.builder().transcashId(transcashId).build();

        MoqueryTranscashIdType type = MoqueryTranscashIdType.Chargeitem;
        ComparedData data = queryInputFactory.getComparedData(type, case1);
        validataComparedData(data);
        validataComparedDataInMoquery(data, type);
        assertEquals(transcashId, data.getData());
        assertEquals("moquery", data.getQueryService());
        assertEquals("chargeitem", data.getQueryInput().getParam().getQueryitem().getTablename());
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

	private String getTelnumFromComparedData(ComparedData data) {
		return data.getQueryInput().getParam().getTelnum();
	}
}
