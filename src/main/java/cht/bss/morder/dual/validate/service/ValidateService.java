package cht.bss.morder.dual.validate.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.factory.QueryServiceFactory;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class ValidateService {

	@Autowired
	private QueryServiceFactory serviceFactory;

	public TestCase validateCheck(TestCase testCase) {

		List<ComparedData> comparedDataList = queryResult(testCase);
		testCase.setComparedData(comparedDataList);
		writeToString(testCase);
		return testCase;
	}

	protected void writeToString(TestCase testcase) {

		List<ComparedData> results = testcase.getComparedData();

		String dataPath = testcase.getDataPath();

		for (ComparedData result : results) {
			try {
				writeCht(dataPath, result);
				writeIISI(dataPath, result);
			} catch (IOException e) {
				throw new RuntimeException("IOException!", e);
			}
		}

	}

	private void writeIISI(String dataPath, ComparedData result) throws IOException {
		String queryService = result.getQueryService();
		String fileName = result.getFileNameInIISI();
		final String folderTemplate = "%s/%s";
		final File outputFile = new File(String.format(folderTemplate, dataPath, queryService), fileName);
		IOUtils.write(result.getDataFromCht(), FileUtils.openOutputStream(outputFile), StandardCharsets.UTF_8);
	}

	private void writeCht(String dataPath, ComparedData result) throws IOException {
		String queryService = result.getQueryService();
		String fileName = result.getFileNameInCHT();
		final String folderTemplate = "%s/%s";
		final File outputFile = new File(String.format(folderTemplate, dataPath, queryService), fileName);
		IOUtils.write(result.getDataFromCht(), FileUtils.openOutputStream(outputFile), StandardCharsets.UTF_8);
	}

	private List<ComparedData> queryResult(TestCase testCase) {
		List<ComparedData> result = new ArrayList<>();
		serviceFactory.getQueryServiceList().stream().map(service -> service.queryTestCase(testCase))
				.forEach(result::addAll);
		return result;
	}

}
