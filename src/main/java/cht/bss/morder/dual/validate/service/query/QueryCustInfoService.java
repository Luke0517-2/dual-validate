package cht.bss.morder.dual.validate.service.query;

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
		try {
			List<ComparedData> comparedDataByTelnum = queryByTelnum(testCase);
			List<ComparedData> comparedDataByCustId = queryByCustId(testCase);
			comparedDataByCustId.addAll(comparedDataByCustId);
			return comparedDataByTelnum;
		} catch (InterruptedException | ExecutionException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	private List<ComparedData> queryByCustId(TestCase testCase) throws InterruptedException, ExecutionException {
		ComparedData comparedData = factory.getComparedData(QueryCustinfoType.custbehavior, testCase);
		CompletableFuture<List<ComparedData>> result = queryResult(comparedData);
		return result.get();
	}

	private List<ComparedData> queryByTelnum(TestCase testCase) throws InterruptedException, ExecutionException {
		ComparedData comparedData = factory.getComparedData(QueryCustinfoType.telnum, testCase);
		CompletableFuture<List<ComparedData>> result = queryResult(comparedData);
		return result.get();
	}

}
