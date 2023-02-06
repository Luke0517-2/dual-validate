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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"dual-validate.checkTable=false"})
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

        ComparedData comparedDataForTelnum = queryInputFactory.getComparedData(QueryCustinfoType.telnum, case1);

        validataComparedData(comparedDataForTelnum);
        assertEquals(comparedDataForTelnum.getData(), telnum);
        assertEquals(comparedDataForTelnum.getTable(), "telnum");
        assertEquals("querycustinfo", comparedDataForTelnum.getQueryService());

        ComparedData comparedDataForCustbehavior = queryInputFactory.getComparedData(QueryCustinfoType.custbehavior, case1);

        validataComparedData(comparedDataForCustbehavior);
        assertEquals(comparedDataForCustbehavior.getData(), custid);
        assertEquals(comparedDataForCustbehavior.getTable(), "custbehavior");
        assertEquals("querycustinfo", comparedDataForTelnum.getQueryService());
    }

	@Test
	public void testGetComparedData_In_QrySaleBehavior() {
        final String telnum = "0912345678";
        final String custid = "Helloworld";
        TestCase case1 = TestCase.builder().telNum(telnum).custId(custid).build();

        ComparedData comparedData = queryInputFactory.getComparedData(QrySalebehaviorType.qrySalebehavior, case1);
        String telnumFromComparedData = getTelnumFromComparedData(comparedData);
        assertFalse(StringUtils.isEmpty(telnumFromComparedData));
        assertEquals(telnum, telnumFromComparedData);

        validataComparedData(comparedData);
        assertEquals(telnum, comparedData.getData());
        assertEquals("qrysalebehavior", comparedData.getTable());
        assertEquals("qrysalebehavior", comparedData.getQueryService());
    }

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTelnumType() {
        final String contractId = "Helloworld";
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).contract(contractId).build();

        MoqueryContractWithTelnumType type = MoqueryContractWithTelnumType.Contractret;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(contractId + "&" + telnum, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
    }

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryOrderNoType() {
        final String orderNo = "Helloworld";
        TestCase case1 = TestCase.builder().orderno(orderNo).build();

        MoqueryOrderNoType type = MoqueryOrderNoType.Modelinsrec;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(orderNo, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
    }

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractType() {
        final String contractId = "Helloworld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractType type = MoqueryContractType.AgentMobileSet;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(contractId, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
    }

	@Test
	public void testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcType() {
        final String spsvc = "Helloworld";
        TestCase case1 = TestCase.builder().spsvc(spsvc).build();

        MoquerySpsvcType type = MoquerySpsvcType.Mdsvc;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(spsvc, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
    }
	
	
	@Autowired
	private DynamicRuleSampleProperties dynamicsRules;
	@Test
	public void test20230204() {
		dynamicsRules.getRules().forEach(rule->{
			MoqueryEnumForTwiceQuery aa= MoqueryEnumForTwiceQuery.valueOf(rule);
			assertEquals( MoqueryEnumForTwiceQuery.MoqueryRentCustNo, aa);
			MoqueryEnumInterface[] tables1 = aa.getMoqueryEnumForFirstPhases();
			MoqueryEnumInterface tables2 = aa.getMoqueryEnumSecondPhase();
			System.out.println(aa);
		});;
//		String rule ="MoqueryRentCustNo";
		
    }
	
	
	@Test
	public void testDynamic () {
		ArrayList<MoqueryContractWithDateType> arrayList = new ArrayList<MoqueryContractWithDateType>();
		dynamicsRules.getRules().forEach(rule -> {
			System.out.println(rule);
//			MoqueryContractWithDateType moqueryContractWithDateType = MoqueryContractWithDateType.valueOf(rule);
//			arrayList.add(moqueryContractWithDateType);
		});
//		Object[] array = arrayList.toArray();
		
//		assertArrayEquals(new MoqueryContractWithDateType[]{
//			MoqueryContractWithDateType.Chgcustrec,
//			MoqueryContractWithDateType.DataShareRecLog,
//			MoqueryContractWithDateType.Empbusiness
//		}, array );
	}
	
	
    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcTypeCase2() {
        final String spsvc = "Helloworld";
        TestCase case1 = TestCase.builder().spsvc(spsvc).build();

        MoquerySpsvcType type = MoquerySpsvcType.F3svc;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(spsvc + "&" + yesterday + "&" + yesterday, comparedData.getData());
        assertEquals("f3svc", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumType type = MoqueryTelnumType.Agent5id;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(telnum, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithDateType type = MoqueryContractWithDateType.Telsusptype;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(contractId + "&" + yesterday, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("telsusptype", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithMingGuoDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithMingGuoDateType type = MoqueryContractWithMingGuoDateType.Officialfee;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(contractId + "&" + getMingGouString(), comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("officialfee", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTwoDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithTwoDateType type = MoqueryContractWithTwoDateType.Discnttype;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(contractId + "&" + yesterday + "&" + yesterday, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("discnttype", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithDateType type = MoqueryTelnumWithDateType.ModelinsrecShop;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(telnum + "&" + yesterday, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("modelinsrec_shop", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithMingGuoDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithMingGuoDateType type = MoqueryTelnumWithMingGuoDateType.Recotemp;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(telnum + "&" + getMingGouString(), comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("recotemp", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithTwoDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithTwoDateType type = MoqueryTelnumWithTwoDateType.Empdiscntrec;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(telnum + "&" + yesterday + "&" + yesterday, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("empdiscntrec", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumsWithDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumsWithDateType type = MoqueryTelnumsWithDateType.Workingrecord;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(yesterday + "&" + telnum + "&" + telnum, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("workingrecord", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryRentCustNoType() {
        final String rentcustno = "123456789";
        TestCase case1 = TestCase.builder().rentcustno(rentcustno).build();

        MoqueryRentCustNoType type = MoqueryRentCustNoType.Pascustomer;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(rentcustno, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("pascustomer", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTranscashIdType() {
        final String transcashId = "123456789";
        TestCase case1 = TestCase.builder().transcashId(transcashId).build();

        MoqueryTranscashIdType type = MoqueryTranscashIdType.Chargeitem;
        ComparedData comparedData = queryInputFactory.getComparedData(type, case1);
        validataComparedData(comparedData);
        validataComparedDataInMoquery(comparedData, type);
        assertEquals(transcashId, comparedData.getData());
        assertEquals("moquery", comparedData.getQueryService());
        assertEquals("chargeitem", comparedData.getQueryInput().getParam().getQueryitem().getTablename());
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
