package cht.bss.morder.dual.validate.service;

import java.util.ArrayList;
import java.util.List;

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

		List<ComparedData> comparedData = queryResult(testCase);
		testCase.setComparedData(comparedData);
		return testCase;
	}

	private List<ComparedData> queryResult(TestCase testCase) {
		List<ComparedData> result = new ArrayList<>();
		serviceFactory.getQueryServiceList().stream().map(service -> service.queryTestCase(testCase))
				.forEach(result::addAll);
		return result;
	}

}
