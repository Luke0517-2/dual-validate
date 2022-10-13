package cht.bss.morder.dual.validate.service.query;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cht.bss.morder.dual.validate.common.exceptions.BusinessException;
import cht.bss.morder.dual.validate.enums.QueryCustinfoType;
import cht.bss.morder.dual.validate.factory.QueryCustInfoInputFactory;
import cht.bss.morder.dual.validate.vo.ComparedData;
import cht.bss.morder.dual.validate.vo.TestCase;

@Service
public class QueryCustInfoService extends QueryService {

	@Autowired
	private QueryCustInfoInputFactory factory;

	@Override
	protected List<ComparedData> queryData(TestCase testCase) {

		ComparedData comparedDataByTelnum = queryByTelnum(testCase);
		ComparedData comparedDataByCustId = queryByCustId(testCase);
		return Arrays.asList(new ComparedData[] { comparedDataByTelnum, comparedDataByCustId });
	}

	private ComparedData queryByCustId(TestCase testCase) {
		ComparedData comparedData = factory.getComparedData(QueryCustinfoType.custbehavior, testCase);
		CompletableFuture<ComparedData> result = queryResult(comparedData);
		try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			comparedData.setError("執行發生錯誤");
			return comparedData;
		}
	}

	private ComparedData queryByTelnum(TestCase testCase) {
		ComparedData comparedData = factory.getComparedData(QueryCustinfoType.telnum, testCase);
		CompletableFuture<ComparedData> result = queryResult(comparedData);
		try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			comparedData.setError("執行發生錯誤");
			return comparedData;
		}
	}

}
