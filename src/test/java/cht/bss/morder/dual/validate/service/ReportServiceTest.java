package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Params;
import cht.bss.morder.dual.validate.vo.QueryInput;
import cht.bss.morder.dual.validate.vo.Report;
import cht.bss.morder.dual.validate.vo.TestCase;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ReportServiceTest {

	@Autowired
	private ReportService reportService;

	private final static String root = "./sample";
	private final static String fileName = "reportSample";
	private final static String resultEqual = "一致";
	private final static String resultNonEqual = "不相同";
	private final static String resultFormatError = "格式錯誤";
	private final static String resultNormal = "門號正常";
	private final static String resultDiff = "有差異";

	private final static String jsonCase1 = "{\"BMS\":{\"Status\":\"0\",\"Msg\":\"成功\",\"TelNum\":\"0928879901\",\"Data\":{\"Renter\":{\"RentId\":\"G499280561\"}}}}";
	private final static String jsonCase2 = "{\"BMS\":{\"Status\":\"0\",\"Msg\":\"失敗\",\"TelNum\":\"0928879901\",\"Data\":{\"Renter\":{\"RentId\":\"mockERROR\"}}}}";
	private final static String jsonStructureError = "{\"BMS\":{\"Status\":\"0\",\"Msg\":\"失敗\",\"CustID\":\"G499280561\",\"Data\":[{\"notsmsbill\":\"N\"";

	@TempDir
	static Path mockRootPath;

	@Test
	@Order(1)
	void test_startReport() {
		Report report = reportService.startReport();
		assertNotNull(report);
		String uuid = report.getUuid();
		log.info("report uuid:{}", uuid);
		assertEquals(report, reportService.getReportByUuid(uuid));

		reportService.cleanUpReportByUuid(uuid);
	}

	@Test
	@Order(2)
	void test_processReport() throws IOException {
		Report testReport = reportService.startReport();
		String uuid = testReport.getUuid();

		TestCase jUnitTestCase = usingJUnitTestCase();

		reportService.addTestCaseToReport(uuid, jUnitTestCase);
		byte[] datas = reportService.processReport(testReport);
		assertNotNull(datas);

		File outputDir = new File(root);
		if (!outputDir.exists())
			outputDir.mkdir();

		File file = new File(root + File.separator + fileName + ".xlsx");
		FileUtils.writeByteArrayToFile(file, datas);
		reportService.cleanUpReportByUuid(uuid);
	}

	@Test
	@Order(3)
	void test_getCurrentReportWithZip() throws InvalidFormatException, IOException {
		Report testReport = reportService.startReport();
		String uuid = testReport.getUuid();

		TestCase jUnitTestCase = usingJUnitTestCase();

		reportService.addTestCaseToReport(uuid, jUnitTestCase);
		byte[] zip = reportService.getCurrentReportWithZip(testReport);
		assertNotNull(zip);

		File outputDir = new File(root);
		if (!outputDir.exists())
			outputDir.mkdir();

		File file = new File(root + File.separator + fileName + ".zip");
		FileUtils.writeByteArrayToFile(file, zip);
		reportService.cleanUpReportByUuid(uuid);
	}

	@Test
	@Order(5)
	void test_checkSingleComparedDataResult() throws IOException {

		List<String[]> assertEqualsPair = new ArrayList<>();
		assertEqualsPair.add(new String[] { null, null });
		assertEqualsPair.add(new String[] { null, "" });
		assertEqualsPair.add(new String[] { jsonCase1, jsonCase1 });
		assertEqualsPair.add(new String[] { jsonStructureError, jsonStructureError });

		assertEqualsPair.stream().forEach(pair -> {
			validDataInExcelIsEquals(pair[0], pair[1]);
			validResultInExcelIsEquals(pair[0], pair[1]);
		});

		List<String[]> assertNotEqualsPair = new ArrayList<>();
		assertNotEqualsPair.add(new String[] { null, jsonCase1 });
		assertNotEqualsPair.add(new String[] { null, jsonStructureError });
		assertNotEqualsPair.add(new String[] { jsonCase1, jsonCase2 });

		assertNotEqualsPair.stream().forEach(pair -> {
			validDataInExcelIsNotEquals(pair[0], pair[1]);
			validResultInExcelIsNotEquals(pair[0], pair[1]);
		});
	}

	private void validResultInExcelIsNotEquals(String dataFromCht, String dataFromIISI) {
		try {
			String result = retrieveTestCaseResultFromExcel(dataFromCht, dataFromIISI, false);
			log.info("Excel-Result : C2:{}",result);
			assertEquals(resultDiff, result);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private void validResultInExcelIsEquals(String dataFromCht, String dataFromIISI) {
		try {
			String result = retrieveTestCaseResultFromExcel(dataFromCht, dataFromIISI, true);
			log.info("Excel-Result : C2:{}",result);
			assertEquals(resultNormal, result);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private String retrieveTestCaseResultFromExcel(String dataFromCht, String dataFromIISI , Boolean isAllCorrect) throws IOException {
		return retrieveTestCaseResultFromExcel("0912345678", "Helloworld", dataFromCht, dataFromIISI, isAllCorrect);
	}

	private String retrieveTestCaseResultFromExcel(String telnum, String custId, String dataFromCht, String dataFromIISI, Boolean isAllCorrect) throws IOException {

		Report report = mockInstanceOfReport(telnum, custId, dataFromCht, dataFromIISI,isAllCorrect);

		try (XSSFWorkbook book = new XSSFWorkbook()) {
			reportService.generateExcelByReport(book, report);
			/*門號的比較結果固定放在sheet-Result的C2儲存格。*/
			return retrieveValueOfExcelCell(book, 1, "C2");
		}
	}

	private void validDataInExcelIsNotEquals(String dataFromCht, String dataFromIISI) {
		try {
			String result = retrieveComparedResultFromExcel(dataFromCht, dataFromIISI);
			log.info("NotEquals -> cht:{}, iisi:{}", dataFromCht, dataFromIISI);
			assertEquals(resultNonEqual, result);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private void validDataInExcelIsEquals(String dataFromCht, String dataFromIISI) {
		try {
			String result = retrieveComparedResultFromExcel(dataFromCht, dataFromIISI);
			log.info("Equals -> cht:{}, iisi:{}", dataFromCht, dataFromIISI);
			assertEquals(resultEqual, result);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private String retrieveComparedResultFromExcel(String dataFromCht, String dataFromIISI) throws IOException {
		return retrieveComparedResultFromExcel("0912345678", "Helloworld", dataFromCht, dataFromIISI);
	}

	private String retrieveComparedResultFromExcel(String telnum, String custId, String dataFromCht,
			String dataFromIISI) throws IOException {

		Report report = mockInstanceOfReport(telnum, custId, dataFromCht, dataFromIISI,true);

		try (XSSFWorkbook book = new XSSFWorkbook()) {
			reportService.generateExcelByReport(book, report);
			/*CHT、IISI的比較結果固定放在sheet-TestCase的G2儲存格。*/
			return retrieveValueOfExcelCell(book, 0,"G2");
		}

	}

	/**
	 * 從目標sheet取出目標cell欄位值
	 * @param book
	 * @param sheetIndex 目標sheet
	 * @param cellRef 目標cell
	 * @return
	 */
	private String retrieveValueOfExcelCell(XSSFWorkbook book,int sheetIndex, String cellRef){
		XSSFSheet sheet = (XSSFSheet) book.getSheetAt(sheetIndex);

		CellReference cellReference = new CellReference(cellRef);
		Row row = sheet.getRow(cellReference.getRow());
		Cell cell = row.getCell(cellReference.getCol());
		String cellValueOfCompareResult = cell.getStringCellValue();

		return cellValueOfCompareResult;
	}

	private TestCase usingJUnitTestCase() {
		String telnum = "0912345678";
		String custid = "Helloworld";

		TestCase testCase = TestCase.builder().telNum(telnum).custId(custid).build();

		List<ComparedData> comparedDataList = new ArrayList<>();

		comparedDataList.add(mockDataOfQuerycustinfo(null, null, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(null, "", "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(null, jsonCase1, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(null, jsonStructureError, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo("", "", "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo("", jsonCase1, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo("", jsonStructureError, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(jsonCase1, jsonCase1, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(jsonCase1, jsonCase2, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(jsonCase1, jsonStructureError, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(jsonStructureError, jsonStructureError, "telnum"));

		testCase.setComparedData(comparedDataList);
		return testCase;
	}

	// 把add進去的假資料包裝成method、setBy: 1. queryService
	private ComparedData mockDataOfQuerycustinfo(String dataFromCht, String dataFromIISI, String table) {
		ComparedData data = ComparedData.builder().dataFromCht(dataFromCht).dataFromIISI(dataFromIISI)
				.queryService("querycustinfo").table(table).queryInput(mockInstanceOfQueryInput()).build();
		return data;
	}
	
	private QueryInput mockInstanceOfQueryInput() {
		return QueryInput.builder().param(Params.builder().build()).build();
	}

	private Report mockInstanceOfReport(String telnum, String custId, String dataFromCht, String dataFromIISI, Boolean isAllCorrect){
		List<ComparedData> list = new ArrayList<>();
		list.add(ComparedData.builder().dataFromCht(dataFromCht).dataFromIISI(dataFromIISI).build());
		TestCase testCase = TestCase.builder().telNum(telnum).custId(custId).comparedData(list).dataPath("./").isAllCorrect(isAllCorrect).build();
		List<TestCase> caseList = new ArrayList<>();
		caseList.add(testCase);

		Report report = new Report();
		report.setTestCases(caseList);

		return report;
	}
}
