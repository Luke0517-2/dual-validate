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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.CalendarUtil;
import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
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
			final Report report = this.uuidReportMap.get(uuid);
			report.getTestCases().add(testCase);
			final String dataPath = buildDataPath(report, testCase);
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
		// TODO: 產生報表
//		byte[] content = null;
//		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
//			final XSSFSheet sheet = workbook.createSheet("TestCases");
//			final Row titleRow = sheet.createRow(0);
//			final String[] columns = new String[] { "案例號碼", "預計結果", "實際結果", "比對結果", "錯誤原因", "檔案路徑" };
//
//			int t = 0;
//			for (final String columnName : columns) {
//				final Cell cell = titleRow.createCell(t++);
//				cell.setCellValue(columnName);
//			}
//
//			final List<TestCase> testCases = report.getTestCases();
//
//			int rowNum = 0;
//
//			for (final TestCase testCase : testCases) {
//				final Row dataRow = sheet.createRow(++rowNum);
//
//				final String[] path = testCase.getDataPath().split("/");
//
//				int r = 0;
//				dataRow.createCell(r++).setCellValue(testCase.getCaseNo());
//				dataRow.createCell(r++).setCellValue(testCase.getExpectedResult());
//				dataRow.createCell(r++).setCellValue(testCase.getActualResult());
//				dataRow.createCell(r++).setCellValue(testCase.getCompareResult().toString());
//				dataRow.createCell(r++).setCellValue(testCase.getErrorReason());
//				dataRow.createCell(r++).setCellValue("/" + path[path.length - 1]);
//			}
//
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			workbook.write(out);
//			content = out.toByteArray();
//		} catch (IOException e) {
//			log.error("", e);
//		}

		return null;
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
		final boolean successMkDir = newDir.mkdir();
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

		final String compFileName = String.format("report_%s.zip", sdf.format(date));
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
	 * Gets the report by uiid.
	 *
	 * @param uuid the uuid
	 * @return the report by uiid
	 */
	public Report getReportByUiid(final String uuid) {
		return this.uuidReportMap.get(uuid);
	}

	/**
	 * Clean up report by uiid.
	 *
	 * @param uuid the uuid
	 */
	public void cleanUpReportByUiid(final String uuid) {
		final Report report = getReportByUiid(uuid);
		if (report != null) {
			final String basePath = report.getBasePath();
			FileUtils.deleteQuietly(new File(basePath));
			this.uuidReportMap.remove(uuid, report);
		}
	}
}
