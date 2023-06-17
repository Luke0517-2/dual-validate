package cht.bss.morder.dual.validate.service.query;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.ObjectProvider;
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
	private ObjectProvider<ComparedData> comparedDataProvider;

	@Override
	protected List<ComparedData> queryData(TestCase testCase) {

		ComparedData comparedDataByTelnum = queryByTelnum(testCase);
		ComparedData comparedDataByCustId = queryByCustId(testCase);
		return Arrays.asList(new ComparedData[] { comparedDataByTelnum, comparedDataByCustId });
	}

	private ComparedData queryByCustId(TestCase testCase) {
		ComparedData comparedData = comparedDataProvider.getObject(QueryCustinfoType.custbehavior, testCase, null);
		comparedData.addIISIParam();
		CompletableFuture<ComparedData> result = queryResult(comparedData);
		try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			comparedData.setError("執行發生錯誤");
			return comparedData;
		}
	}

	private ComparedData queryByTelnum(TestCase testCase) {
		ComparedData comparedData = comparedDataProvider.getObject(QueryCustinfoType.telnum, testCase, null);
		comparedData.addIISIParam();
		CompletableFuture<ComparedData> result = queryResult(comparedData);
		try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			comparedData.setError("執行發生錯誤");
			return comparedData;
		}
	}
//	private List<ComparedData> queryByCustinfoType(Enum<QueryCustinfoType> type,TestCase testCase) throws InterruptedException, ExecutionException {
//		ComparedData comparedData = factory.getComparedData(type, testCase);
//		CompletableFuture<List<ComparedData>> result = queryResult(comparedData);
//		return result.get();
//	}

}
