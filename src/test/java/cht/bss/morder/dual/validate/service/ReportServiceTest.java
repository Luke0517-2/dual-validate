package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Arrays;
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

//		reportService.cleanUpReportByUuid(uuid);
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
//		reportService.cleanUpReportByUuid(uuid);
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
//		reportService.cleanUpReportByUuid(uuid);
	}

	@Test
	@Order(5)
	void test_checkSingleComparedDataResult() throws IOException {

		List<String[]> assertEqualsPair = new ArrayList<>();
//		assertEqualsPair.add(new String[] { null, null });
//		assertEqualsPair.add(new String[] { null, "" });
		assertEqualsPair.add(new String[] { jsonCase1, jsonCase1 });
		assertEqualsPair.add(new String[] { jsonStructureError, jsonStructureError });

		assertEqualsPair.stream().forEach(pair -> validDataInExcelIsEquals(pair[0], pair[1]));

		List<String[]> assertNotEqualsPair = new ArrayList<>();
		assertNotEqualsPair.add(new String[] { null, jsonCase1 });
		assertNotEqualsPair.add(new String[] { null, jsonStructureError });
		assertNotEqualsPair.add(new String[] { jsonCase1, jsonCase2 });

		assertNotEqualsPair.stream().forEach(pair -> validDataInExcelIsNotEquals(pair[0], pair[1]));
	}

	private void validDataInExcelIsNotEquals(String dataFromCht, String dataFromIISI) {
		try {
			String result = retrieveComparedResultFromExcel(dataFromCht, dataFromIISI);
			log.info("cht:{}, iisi:{}", dataFromCht, dataFromIISI);
			assertEquals(resultNonEqual, result);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private void validDataInExcelIsEquals(String dataFromCht, String dataFromIISI) {
		try {
			String result = retrieveComparedResultFromExcel(dataFromCht, dataFromIISI);
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

		List<ComparedData> list = new ArrayList<>();
		list.add(ComparedData.builder().dataFromCht(dataFromCht).dataFromIISI(dataFromIISI).build());
		TestCase testCase = TestCase.builder().telNum(telnum).custId(custId).comparedData(list).dataPath("./").build();
		List<TestCase> caseList = new ArrayList<>();
		caseList.add(testCase);

		Report report = new Report();
		report.setTestCases(caseList);

		try (XSSFWorkbook book = new XSSFWorkbook()) {
			reportService.generateExcelByReport(book, report);
			return retrieveValueOfExcelCellF2(book);
		}

	}

	/**
	 * 取出F2儲存格value。 (CHT、IISI的比較結果固定放在Excel sheet的F2儲存格。)
	 * 
	 * @param filePath 目標Excel檔路徑
	 * @return 位置為F2 cell value
	 */
	private String retrieveValueOfExcelCellF2(XSSFWorkbook book) {
		XSSFSheet sheet = (XSSFSheet) book.getSheetAt(0);

		CellReference cellReference = new CellReference("F2");
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
}
