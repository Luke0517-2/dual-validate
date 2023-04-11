package cht.bss.morder.dual.validate.factory;

import cht.bss.morder.dual.validate.enums.*;
import cht.bss.morder.dual.validate.vo.*;
import cht.bss.morder.dual.validate.vo.json.AbstractJSONPathModel;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 驗證 output.json 中的 queryType / tableName 和 enum 中的 type / tableName 是否一致。
 *
 * */
@SpringBootTest
public class QueryInputFactoryTest {

	@Autowired
	private ObjectProvider<ComparedData> comparedDataProvider;

    private final String yesterday = LocalDate.now().minusDays(1).toString();
    private final MoqueryContractType[] specialContractType = {MoqueryContractType.SpecsvcidMN, MoqueryContractType.SpecsvcidMV, MoqueryContractType.SpecsvcidF3};

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

        ComparedData comparedDataForTelnum = comparedDataProvider.getObject(QueryCustinfoType.telnum, case1,null);

        validataComparedData(comparedDataForTelnum);
        assertEquals(comparedDataForTelnum.getData(), telnum);
        assertEquals(comparedDataForTelnum.getTable(), "telnum");
        assertEquals("querycustinfo", comparedDataForTelnum.getQueryService());

        ComparedData comparedDataForCustbehavior = comparedDataProvider.getObject(QueryCustinfoType.custbehavior, case1,null);

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

        ComparedData comparedData = comparedDataProvider.getObject(QrySalebehaviorType.qrySalebehavior, case1,null);
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

        MoqueryContractWithTelnumType[] enumValueAll = MoqueryContractWithTelnumType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";

                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(contractId + "&" + telnum, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryOrderNoType() {
        final String orderNo = "Helloworld";
        TestCase case1 = TestCase.builder().orderno(orderNo).build();

        MoqueryOrderNoType[] enumValueAll = MoqueryOrderNoType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";

                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(orderNo, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    /*
     * 由於 MoqueryContractType 中有幾個的 contentTemplate 不太一樣，
     * 故將其抽出放置testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcType 檢驗
     * */
    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractType() {
        final String contractId = "Helloworld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractType[] enumValueAll = MoqueryContractType.values();
        Arrays.stream(enumValueAll)
                .filter(enumValue -> !ArrayUtils.contains(specialContractType, enumValue))
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";

                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(contractId, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractSpvcType() {
        final String contractId = "Helloworld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        Arrays.stream(specialContractType)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";

                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(contractId + "&" + value.getContentTemplate().split("&")[1], comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoquerySpsvcType() {
        final String spsvc = "Helloworld";
        TestCase case1 = TestCase.builder().spsvc(spsvc).build();

        MoquerySpsvcType[] enumValueAll = MoquerySpsvcType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";

                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    String contentTemplate = value.getContentTemplate();
                    switch (contentTemplate) {
                        case "%s":
                            assertEquals(spsvc, comparedData.getData(), " Something wrong with " + value);
                            break;
                        case "%s&%s&%s":
                            assertEquals(spsvc + "&" + yesterday + "&" + yesterday, comparedData.getData(), " Something wrong with " + value);
                            break;
                        default:
                            throw new RuntimeException();
                    }
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumType[] enumValueAll = MoqueryTelnumType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(telnum, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithDateType[] enumValueAll = MoqueryContractWithDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(contractId + "&" + yesterday, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithMingGuoDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithMingGuoDateType[] enumValueAll = MoqueryContractWithMingGuoDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(contractId + "&" + getMingGouString(), comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryContractWithTwoDateType() {
        final String contractId = "HelloWorld";
        TestCase case1 = TestCase.builder().contract(contractId).build();

        MoqueryContractWithTwoDateType[] enumValueAll = MoqueryContractWithTwoDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(contractId + "&" + yesterday + "&" + yesterday, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithDateType[] enumValueAll = MoqueryTelnumWithDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(telnum + "&" + yesterday, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithMingGuoDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithMingGuoDateType[] enumValueAll = MoqueryTelnumWithMingGuoDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(telnum + "&" + getMingGouString(), comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumWithTwoDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumWithTwoDateType[] enumValueAll = MoqueryTelnumWithTwoDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(telnum + "&" + yesterday + "&" + yesterday, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTelnumsWithDateType() {
        final String telnum = "0912345678";
        TestCase case1 = TestCase.builder().telNum(telnum).build();

        MoqueryTelnumsWithDateType[] enumValueAll = MoqueryTelnumsWithDateType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(yesterday + "&" + telnum + "&" + telnum, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryRentCustNoType() {
        final String rentcustno = "123456789";
        TestCase case1 = TestCase.builder().rentcustno(rentcustno).build();

        MoqueryRentCustNoType[] enumValueAll = MoqueryRentCustNoType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(rentcustno, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
    }

    @Test
    public void testGetComparedData_In_MoqueryInputFactory_MoqueryTranscashIdType() {
        final String transcashId = "123456789";
        TestCase case1 = TestCase.builder().transcashId(transcashId).build();

        MoqueryTranscashIdType[] enumValueAll = MoqueryTranscashIdType.values();
        Arrays.stream(enumValueAll)
                .forEach(value -> {
                    ComparedData comparedData = comparedDataProvider.getObject(null, case1 , value);
                    String jsonFilePath = "./jsonsample/" + value.toString().toLowerCase() + "_output.json";
                    try {
                        compareJsonWithEnum(value, jsonFilePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    assertNotNull(value.getTableName());
                    validataComparedData(comparedData);
                    validataComparedDataInMoquery(comparedData, value);
                    assertEquals(transcashId, comparedData.getData());
                    assertEquals("moquery", comparedData.getQueryService());
                    assertEquals(value.getTableName(), comparedData.getQueryInput().getParam().getQueryitem().getTablename());
                });
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

    private void compareJsonWithEnum(MoqueryEnumInterface moqueryEnum, String jsonFilePath) throws IOException {
        String json = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
        AbstractJSONPathModel abstractJSONPathModel = new AbstractJSONPathModel(JsonPath.parse(json)) {
            @Override
            public String getValue() {
                return null;
            }
        };
        assertNotNull(json);
        assertNotNull(abstractJSONPathModel);
        Optional<Object> tableNameParamsFromJSON = abstractJSONPathModel.getSingleValueByParams("$..BMS.tablename");
        Optional<Object> queryTypeParamsFromJSON = abstractJSONPathModel.getSingleValueByParams("$..BMS.querytype");
        final String tableNameFromJSON = tableNameParamsFromJSON.map(Object::toString).orElse(null);
        final String queryTypeFromJSON = queryTypeParamsFromJSON.map(Object::toString).orElse(null);
        assertEquals(queryTypeFromJSON, moqueryEnum.getType());
        assertEquals(tableNameFromJSON, moqueryEnum.getTableName());
    }
}
