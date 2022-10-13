package cht.bss.morder.dual.validate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.vo.ComparedData;
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
		assertEquals(report, reportService.getReportByUiid(uuid));

		reportService.cleanUpReportByUiid(uuid);
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
		reportService.cleanUpReportByUiid(uuid);
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
		reportService.cleanUpReportByUiid(uuid);
	}

	@Test
	@Order(4)
	void test_readExcel2CheckCellValueOfCompareResult() {
		String cellValueOfCompareResult = retrieveValueOfExcelCellF2(root + File.separator + fileName + ".xlsx");
		assertEquals(resultEqual, cellValueOfCompareResult);
	}
	
	@Test
	@Order(5)
	void test_checkSingleComparedDataResult() throws IOException {
		String singleComparedDataResult;
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", null, null);
//		log.info("singleComparedDataResult: {}", singleComparedDataResult);
		assertEquals(resultEqual, singleComparedDataResult);

		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", null, "");
		assertEquals(resultEqual, singleComparedDataResult);
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", null, jsonCase1);
		assertEquals(resultNonEqual, singleComparedDataResult);
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", null, jsonStructureError);
		assertEquals(resultNonEqual, singleComparedDataResult);
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", jsonCase1, jsonCase1);
		assertEquals(resultEqual, singleComparedDataResult);
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", jsonCase1, jsonCase2);
		assertEquals(resultNonEqual, singleComparedDataResult);
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", jsonCase1, jsonStructureError);
		assertEquals(resultFormatError, singleComparedDataResult);
		
		singleComparedDataResult = singleComparedDataWrite2ExcelAndRetrieve("0912345678", "Helloworld", jsonStructureError, jsonStructureError);
		assertEquals(resultEqual, singleComparedDataResult);
	}

	/**
	 * 一次寫入一筆資料到Excel。並取出該筆資料CHT、IISI比較結果的儲存格 value 回傳。
	 * (在單元測試驗證比較結果的的設計為每次只寫入一筆，讀取驗證一筆。)
	 * 
	 * @param telnum
	 * @param custId
	 * @param dataFromCht
	 * @param dataFromIISI
	 * @return
	 */
	private String singleComparedDataWrite2ExcelAndRetrieve(String telnum, String custId, String dataFromCht, String dataFromIISI) {
		String mockRootPathName = mockRootPath.toString();
		String mockedExcelPath = mockRootPathName + File.separator + "singleCaseAssert.xlsx";
		
		Report testReport = reportService.startReport();
		String uuid = testReport.getUuid();

		TestCase testCase = TestCase.builder().telNum(telnum).custId(custId).build();

		List<ComparedData> comparedDataList = new ArrayList<>();
		comparedDataList.add(mockDataOfQuerycustinfo(dataFromCht, dataFromIISI, "telnum"));

		testCase.setComparedData(comparedDataList);

		reportService.addTestCaseToReport(uuid, testCase);
		byte[] datas = reportService.processReport(testReport);
		assertNotNull(datas);

		File outputDir = new File(root);
		if (!outputDir.exists())
			outputDir.mkdir();

		File file = new File(mockedExcelPath);
		try {
			FileUtils.writeByteArrayToFile(file, datas);
		} catch (IOException e) {
			log.error("", e);
			throw new BusinessException(e.getMessage());
		}
		reportService.cleanUpReportByUiid(uuid);

		String cellValueOfCompareResult = retrieveValueOfExcelCellF2(mockedExcelPath);
		return cellValueOfCompareResult;
	}
	
	/**
	 * 取出F2儲存格value。
	 * (CHT、IISI的比較結果固定放在Excel sheet的F2儲存格。)
	 * 
	 * @param filePath 目標Excel檔路徑
	 * @return 位置為F2 cell value
	 */
	private String retrieveValueOfExcelCellF2(String filePath) {
		try (FileInputStream fis = new FileInputStream(filePath);
				Workbook wb = new XSSFWorkbook(fis);) {
			XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);
			
			CellReference cellReference = new CellReference("F2"); 
			Row row = sheet.getRow(cellReference.getRow());
			Cell cell = row.getCell(cellReference.getCol());
			String cellValueOfCompareResult = cell.getStringCellValue();
			
//			log.info("在cellValueOfCompareResult: {}", cellValueOfCompareResult);
			return cellValueOfCompareResult;
		} catch (IOException e) {
			log.error("", e);
			throw new BusinessException(e.getMessage());
		}
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
		comparedDataList
				.add(mockDataOfQuerycustinfo(jsonCase1, jsonCase1, "telnum"));
		comparedDataList
				.add(mockDataOfQuerycustinfo(jsonCase1, jsonCase2, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(jsonCase1, jsonStructureError, "telnum"));
		comparedDataList.add(mockDataOfQuerycustinfo(jsonStructureError, jsonStructureError, "telnum"));

		testCase.setComparedData(comparedDataList);
		return testCase;
	}

	// 把add進去的假資料包裝成method、setBy: 1. queryService
	private ComparedData mockDataOfQuerycustinfo(String dataFromCht, String dataFromIISI, String table) {
		ComparedData data = ComparedData.builder().dataFromCht(dataFromCht).dataFromIISI(dataFromIISI)
				.queryService("querycustinfo").table(table).build();
		return data;
	}
}
