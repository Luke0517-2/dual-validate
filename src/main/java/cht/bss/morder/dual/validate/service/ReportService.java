package cht.bss.morder.dual.validate.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cht.bss.morder.dual.validate.common.CalendarUtil;
import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.enums.CompareResultType;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.Report;
import cht.bss.morder.dual.validate.vo.TestCase;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.EncryptionMethod;

/**
 * 產生查核驗證用的報表.
 */
@Service
@Slf4j
public class ReportService {

	/** 用來放置比對過程中所產生的比對檔案，可以提供匯出．. */
	@Value("${compare.output.path}")
	private String outputPath;

	/** 有多個測試報告同時執行. */
	private ConcurrentMap<String, Report> uuidReportMap;

	@Autowired
	private ObjectMapper mapper;

	/**
	 * Post construct.
	 */
	@PostConstruct
	public void postConstruct() {
		this.uuidReportMap = new ConcurrentHashMap<String, Report>();
	}

	/**
	 * Adds the test case to report.
	 *
	 * @param testCase the test case
	 */
	public TestCase addTestCaseToReport(final String uuid, final TestCase testCase) {
		if (this.uuidReportMap.keySet().contains(uuid)) {
			log.info("1_testCase in addTestCaseToReport():{}", testCase);
			final Report report = this.uuidReportMap.get(uuid);
			report.getTestCases().add(testCase);
			final String dataPath = buildDataPath(report, testCase);
			log.info("testCase this dataPath:{}", dataPath);
			testCase.setDataPath(dataPath);
			return testCase;
		}
		return testCase;
	}

	/**
	 * 產生Excel報表.
	 *
	 * @param report the report
	 * @return the byte[]
	 */
	public byte[] processReport(final Report report) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			generateExcelByReport(workbook, report);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			byte[] content = out.toByteArray();
			return content;
		} catch (IOException e) {
			log.error("", e);
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 產製Excel內容
	 * 
	 * @param workbook
	 * @param report
	 * @throws IOException
	 */
	protected void generateExcelByReport(XSSFWorkbook workbook, Report report) throws IOException {

		final XSSFSheet sheet = workbook.createSheet("TestCases");

		final String[] columns = new String[] { "比對門號", "證號", "比對類別", "比對參數or表格", "參數欄位或資料", "比對CHT與IISI結果", "說明資訊",
				"檔案路徑" };
		insertTitleRows(sheet, columns);
		insertData(sheet, report);
	}

	/**
	 * 設定Excel顯示欄位名稱
	 * 
	 * @param sheet
	 * @param columns
	 */
	private void insertTitleRows(XSSFSheet sheet, String[] columns) {
		final Row titleRow = sheet.createRow(0);
		for (int indexOfCell = 0; indexOfCell < columns.length; indexOfCell++) {
			titleRow.createCell(indexOfCell).setCellValue(columns[indexOfCell]);
		}
	}

	/**
	 * 產製報告內容，一份report有多項testCase，一項testCase會有多列comparedData
	 * 
	 * @param sheet
	 * @param report
	 */
	private void insertData(XSSFSheet sheet, Report report) {
		List<TestCase> testCases = report.getTestCases();
		int rowNum = 0;
		for (final TestCase testCase : testCases) {
			for (ComparedData comparedData : testCase.getComparedData()) {
				final Row dataRow = sheet.createRow(++rowNum);
				final String[] path = testCase.getDataPath().split("/");

				dataRow.createCell(0).setCellValue(testCase.getTelNum());
				dataRow.createCell(1).setCellValue(testCase.getCustId());
				dataRow.createCell(2).setCellValue(comparedData.getQueryService());
				dataRow.createCell(3).setCellValue(comparedData.getTable());
				dataRow.createCell(4).setCellValue(showDataInReport(comparedData));

				String error = comparedData.getError();
				if (StringUtils.isEmpty(error)) {
					try {
						dataRow.createCell(5).setCellValue(comparedData.getComparedResult(mapper).getValue());
					} catch (JsonProcessingException e) {
						dataRow.createCell(5).setCellValue(CompareResultType.NONEQUAL.getValue());
						dataRow.createCell(6).setCellValue("文字資料不一致，轉成json結構比較時出錯");
					}
				} else {
					try {
						dataRow.createCell(5).setCellValue(comparedData.getComparedResult(mapper).getValue());
						dataRow.createCell(6).setCellValue(error);
					} catch (JsonProcessingException e) {
						dataRow.createCell(5).setCellValue(CompareResultType.NONEQUAL.getValue());
						dataRow.createCell(6).setCellValue("文字資料不一致，轉成json結構比較時出錯");
					}
				}

				dataRow.createCell(7).setCellValue("/" + path[path.length - 1] + "/" + comparedData.getQueryService());
			}
		}
	}

	private String showDataInReport(ComparedData comparedData) {
		String dataInComparedData = comparedData.getData();
		if (dataInComparedData!= null &&  !(dataInComparedData.equals("null"))) {
			return dataInComparedData;
		} else {
			return "無對應參數，不須進行後續查詢";
		}
	}

	/**
	 * Start report.
	 *
	 * @return the report
	 */
	public Report startReport() {
		final UUID uuid = UUID.randomUUID();

		final Report newReport = new Report();
		newReport.setUuid(uuid.toString());
		newReport.setStartDate(CalendarUtil.calendarToOffsetDateTime(Calendar.getInstance()));
		newReport.setTestCases(new ArrayList<TestCase>());

		final String basePath = String.format("%s/%s", this.outputPath, newReport.getUuid());
		newReport.setBasePath(basePath);

		final File newDir = new File(basePath);
		log.info("dir path:{}", newDir.getAbsolutePath());
		final boolean successMkDir = newDir.mkdirs();
		if (!successMkDir) {
			log.warn("Can't mkdir for this report:{}", uuid);
		}
		this.uuidReportMap.put(uuid.toString(), newReport);
		return newReport;
	}

	/**
	 * Builds the data path.
	 *
	 * @param report   the report
	 * @param testCase the test case
	 * @return the string
	 */
	private String buildDataPath(final Report report, final TestCase testCase) {
		return String.format("%s/%s_%s", report.getBasePath(), testCase.getTelNum(), testCase.getCustId());
	}

	/**
	 * 將測試報告與測試過程中所產生的轉換檔案一併打包成ZIP檔案.
	 *
	 * @param report the current report
	 * @return the current report with zip
	 * @throws InvalidFormatException the invalid format exception
	 * @throws IOException            Signals that an I/O exception has occurred.
	 */
	public byte[] getCurrentReportWithZip(final Report report) throws InvalidFormatException, IOException {
		final Date date = Date.from(report.getStartDate().toInstant());
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		final String excelFileName = String.format("report_%s.xlsx", sdf.format(date));

		final byte[] excelStream = processReport(report);
		final StringBuilder uuidPath = new StringBuilder(this.outputPath).append(File.separator)
				.append(report.getUuid());
		final File excelFile = new File(uuidPath.toString() + File.separator + excelFileName);
		FileUtils.writeByteArrayToFile(excelFile, excelStream);

		final File uuidFolder = new File(uuidPath.toString());

		byte[] byteArray = getZipStream(uuidFolder);
		return byteArray;
	}

	private byte[] getZipStream(File folder) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ZipOutputStream zos = new ZipOutputStream(baos);

			Arrays.stream(folder.listFiles()).forEach(file -> {
				addToZip(file, zos, "");
			});
			zos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			log.error("", e);
			throw new RuntimeException();
		}
	}

	private void addToZip(File file, ZipOutputStream zos, String parentFolder) {
		if (file.isDirectory()) {
			Arrays.stream(file.listFiles())
					.forEach(f -> addToZip(f, zos, parentFolder + File.separator + file.getName()));
		} else {
			try {
				ZipParameters params = new ZipParameters();
				String fileName = parentFolder + File.separator + file.getName();
				params.setFileNameInZip(fileName);
				zos.putNextEntry(params);
				zos.write(FileUtils.readFileToByteArray(file));
				zos.closeEntry();
			} catch (IOException e) {
				log.debug(e.getMessage());
				throw new BusinessException(e.getMessage());
			}
		}
	}

	/**
	 * Creates the zip parameters.
	 *
	 * @param encryptionMethod the encryption method
	 * @param aesKeyStrength   the aes key strength
	 * @return the zip parameters
	 */
	protected ZipParameters createZipParameters(final EncryptionMethod encryptionMethod,
			final AesKeyStrength aesKeyStrength, final String fileName) {
		final ZipParameters zipParameters = new ZipParameters();
		zipParameters.setFileNameInZip(fileName);
		zipParameters.setEncryptFiles(false);
		zipParameters.setEncryptionMethod(encryptionMethod);
		zipParameters.setAesKeyStrength(aesKeyStrength);
		return zipParameters;
	}

	/**
	 * Gets the report by uuid.
	 *
	 * @param uuid the uuid
	 * @return the report by uuid
	 */
	public Report getReportByUuid(final String uuid) {
		return this.uuidReportMap.get(uuid);
	}

	/**
	 * Clean up report by uuid.
	 *
	 * @param uuid the uuid
	 */
	public void cleanUpReportByUuid(final String uuid) {
		final Report report = getReportByUuid(uuid);
		if (report != null) {
			final String basePath = report.getBasePath();
			FileUtils.deleteQuietly(new File(basePath));
			this.uuidReportMap.remove(uuid, report);
		}
	}
}
